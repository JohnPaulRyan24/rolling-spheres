package simulation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Simulation {
	public static void muTest() throws FileNotFoundException{
		PrintWriter m = new PrintWriter(new File("MU_"+Constants.WMU+"_"+Constants.bound_vel+"_"+Constants.NUM_OF_SPHERES+".txt"));
		for(int d = 0; d<=100; d+= 1){
			Constants.MU = d/10.0;
		
			initializeSpheres();
			ArrayList<Collision> current;
			double totalAvg = 0;		
			double totalTime = 0;
			for(int i=0;i<Constants.ITERATIONS; i++){		
				totalAvg 	+= Spheres.getAngVel();
				current = Spheres.nextCollision();	
				totalTime += current.get(0).time;
				Spheres.updatePositions(current.get(0).time, totalTime);
				for(Collision c : current){
					c.process();			
				}
			}	
			totalAvg/=Constants.ITERATIONS;
			m.println(Constants.MU+" "+totalAvg);
		}
		m.close();
	}
	public static void wmuTest() throws FileNotFoundException{
		PrintWriter m = new PrintWriter(new File("WMU_"+Constants.MU+"_"+Constants.bound_vel+"_"+Constants.NUM_OF_SPHERES+".txt"));
		for(int d = 0; d<=80; d++){
			Constants.WMU = d/10.0;
		
			initializeSpheres();
			ArrayList<Collision> current;
			double totalAvg = 0;		
			double totalTime = 0;
			for(int i=0;i<Constants.ITERATIONS; i++){		
				totalAvg 	+= Spheres.getAngVel();
				current = Spheres.nextCollision();		
				totalTime += current.get(0).time;
				Spheres.updatePositions(current.get(0).time, totalTime);
				for(Collision c : current){
					c.process();			
				}
			}	
			totalAvg/=Constants.ITERATIONS;
			m.println(Constants.WMU+" "+totalAvg);
		}
		m.close();
	}
	public static void boundvelTest() throws FileNotFoundException{
		PrintWriter m = new PrintWriter(new File("AMP_"+Constants.MU+"_"+Constants.WMU+"_"+Constants.NUM_OF_SPHERES+".txt"));
		for(double d = 5; d<=50; d+= 2){
			Constants.bound_vel = d/10.0;
		
			initializeSpheres();
			ArrayList<Collision> current;
			double totalAvg = 0;		
			double totalTime = 0;
			for(int i=0;i<Constants.ITERATIONS; i++){		
				totalAvg 	+= Spheres.getAngVel();
				current = Spheres.nextCollision();	
				totalTime += current.get(0).time;
				Spheres.updatePositions(current.get(0).time, totalTime);
				for(Collision c : current){
					c.process();			
				}
			}	
			totalAvg/=Constants.ITERATIONS;
			m.println(Constants.bound_vel+" "+totalAvg);
		}
		m.close();
	}
	public static void spherenumTest() throws FileNotFoundException{
		PrintWriter m = new PrintWriter(new File("NUM_"+Constants.MU+"_"+Constants.WMU+"_"+Constants.bound_vel+".txt"));
		double panc_rad;
		for(int d = 20; d<=60; d++){
			Constants.NUM_OF_SPHERES = d;
			panc_rad = 10-(2.11 - 0.0235*d);
			initializeSpheres();
			ArrayList<Collision> current;
			double totalAvg = 0;		
			double totalTime = 0;
			for(int i=0;i<Constants.ITERATIONS; i++){		
				totalAvg 	+= Spheres.getDensity(panc_rad);
				current = Spheres.nextCollision();	
				totalTime += current.get(0).time;
				Spheres.updatePositions(current.get(0).time, totalTime);
				for(Collision c : current){
					c.process();			
//					if(c.isBoundary()){
//						 totalAvg++;
//					}
				}
			}	
			totalAvg/=Constants.ITERATIONS;//(totalTime/1000.0);
			m.println(Constants.NUM_OF_SPHERES+" "+totalAvg);
		}
		m.close();
	}
	public static void testing(String[] args) throws FileNotFoundException{
		setConstants(args);
		if(Constants.MU==-1){
			muTest();
			return;
		}else if(Constants.WMU==-1){
			wmuTest();
			return;
		}else if(Constants.bound_vel==-1){
			boundvelTest();
			return;
		}else{
			spherenumTest();
			return;
		}
		
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		int testing = Integer.parseInt(args[4]);
		if(testing==1){
			testing(args);
			return;
		}
		setConstants(args);
		
		initializeSpheres();
		
		ArrayList<Collision> current;
		PrintWriter p = new PrintWriter(new File("input/output.txt"));
		PrintWriter m = new PrintWriter(new File("input/momenta.txt"));
		PrintWriter hist_s = new PrintWriter(new File("hist_data_s.txt"));
		PrintWriter hist_w = new PrintWriter(new File("hist_data_w.txt"));
		
		long start = System.currentTimeMillis();
		int nonce = 0;
		double temp;
		ArrayList<Double> memory = new ArrayList<Double>();
		double totalAvg = 0, currentMomentum = 0, avg = 0, totalTime = 0;
		currentMomentum = Spheres.getAngVel();
		double pastMomentum = currentMomentum;
		System.out.println("Initial Angular Momentum: "+currentMomentum+"<br>");
		
	//	double totalVar=0;
		//double avgVel = 0;
		for(int i=0;i<Constants.ITERATIONS; i++){
			//avgVel += Spheres.Spheres[0].theta_vel;
			avg			+= currentMomentum;
			totalAvg 	+= currentMomentum;
			//totalVar 	+= Spheres.getVariance();
			
			memory.add(currentMomentum);
			if(memory.size()==1001){
				temp = memory.remove(0);
				avg -= temp;
				m.printf("%f %f\n", totalTime, avg/1000.0); //This is the print for the plot of momentum with time
			}
			current = Spheres.nextCollision();	
			totalTime+=current.get(0).time;
			Spheres.updatePositions(current.get(0).time, totalTime);

			
			for(Collision c : current){
				c.process();
				p.println(c.toString()+" "+nonce);	 //This is the print for the animation
			}
			currentMomentum = Spheres.getAngVel();
			nonce++;
			if(!current.get(0).isSwirl()&&!current.get(0).isBoundary()){
				hist_s.println(currentMomentum-pastMomentum);
			}
			else if(current.get(0).isBoundary()){
				hist_w.println(currentMomentum-pastMomentum);
			}
	
			pastMomentum = currentMomentum;
		
			
		}
	//	System.out.printf("Avg: Vel Ang one ball: -- %.9f\n\n",(avgVel/Constants.ITERATIONS));
		System.out.println("Final Angular Momentum: "+pastMomentum+"<br>");
		
		totalAvg/=Constants.ITERATIONS;
		//totalVar/=Constants.ITERATIONS;
		System.out.println("Average Angular Momentum: "+totalAvg+"<br>");
	//	System.out.println("Average variance of angular frequency: "+totalVar);
		long end = System.currentTimeMillis();
		System.out.printf("Simulation took %d milliseconds.\n",(end-start));
		
		m.close();
		p.close();
		hist_s.close();
		hist_w.close();
	}
	
	
	
	
	public static void setConstants(String[] args) throws FileNotFoundException{
		Constants.MU = Double.parseDouble(args[0]);
		Constants.WMU = Double.parseDouble(args[1]);
		Constants.bound_vel = Double.parseDouble(args[2])/1000.0;
		Constants.NUM_OF_SPHERES = Integer.parseInt(args[3]);
		if(Integer.parseInt(args[5])>0){
			Constants.SPINNING = true;
		}else{
			Constants.SPINNING = false;
		}
		Constants.BOUNDRAD = Double.parseDouble(args[6]);
		
		
		File argFile = new File("args.txt");
		PrintWriter p = new PrintWriter(argFile);
		p.printf("_\n%s\n%s\n_\n_\n_\n%s\n%s\nSphere/sphere friction: %.9f\n"
				+ "Wall/sphere friction: %.9f\nAmplitude: %.9f"
				+ "\nNumber of spheres: %d\n",args[2],args[3], args[5], args[6],
				Constants.MU,Constants.WMU,1000*Constants.bound_vel,Constants.NUM_OF_SPHERES);
		
		p.close();
		
	}

	public static void initializeSpheres() throws FileNotFoundException{
		Scanner input = new Scanner(new File("input/input.txt"));

		input.nextLine();
		Spheres.init();
		Sphere toAdd;
		double pos[];
		double vel[];
		String[] toParse;
		for(int i=0; i<Constants.NUM_OF_SPHERES; i++){
			toAdd = new Sphere();
			pos = new double[Constants.DIMENSIONS];
			vel = new double[Constants.DIMENSIONS];
			toParse = input.nextLine().split(" ");
			for(int j=1;j<=Constants.DIMENSIONS;j++){
				pos[j-1] = Double.parseDouble(toParse[j]);
			}
			toAdd.pos = pos;
			toAdd.theta = 0;
			toAdd.theta_vel = 0;
		
			for(int j=Constants.DIMENSIONS+2;j<2*Constants.DIMENSIONS+2;j++){
				vel[j-Constants.DIMENSIONS-2] = Double.parseDouble(toParse[j]);
			}
			toAdd.vel = vel;
			Spheres.add(toAdd);
		}
		input.close();
	}
	
}
