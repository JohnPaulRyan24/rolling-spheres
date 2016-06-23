package simulation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Simulation {
	public static void muTest() throws FileNotFoundException{
		PrintWriter m = new PrintWriter(new File("MU_"+Constants.WMU+"_"+Constants.bound_vel+"_"+Constants.NUM_OF_SPHERES+".txt"));
		for(int d = 0; d<=30; d+= 1){
			Constants.MU = d/100.0;
		
			initializeSpheres();
			ArrayList<Collision> current;
			double totalAvg = 0;		
			for(int i=0;i<Constants.ITERATIONS; i++){		
				totalAvg 	+= Spheres.getMomenta();
				current = Spheres.nextCollision();			
				Spheres.updatePositions(current.get(0).time);
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
		for(int d = 0; d<=40; d++){
			Constants.WMU = d/10.0;
		
			initializeSpheres();
			ArrayList<Collision> current;
			double totalAvg = 0;		
			for(int i=0;i<Constants.ITERATIONS; i++){		
				totalAvg 	+= Spheres.getMomenta();
				current = Spheres.nextCollision();			
				Spheres.updatePositions(current.get(0).time);
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
		for(double d = 5; d<=90; d+= 5){
			Constants.bound_vel = d/10.0;
		
			initializeSpheres();
			ArrayList<Collision> current;
			double totalAvg = 0;		
			for(int i=0;i<Constants.ITERATIONS; i++){		
				totalAvg 	+= Spheres.getMomenta();
				current = Spheres.nextCollision();			
				Spheres.updatePositions(current.get(0).time);
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
		for(int d = 1; d<=13; d++){
			Constants.NUM_OF_SPHERES = d;
		
			initializeSpheres();
			ArrayList<Collision> current;
			double totalAvg = 0;		
			for(int i=0;i<Constants.ITERATIONS; i++){		
				totalAvg 	+= Spheres.getMomenta();
				current = Spheres.nextCollision();			
				Spheres.updatePositions(current.get(0).time);
				for(Collision c : current){
					c.process();			
				}
			}	
			totalAvg/=Constants.ITERATIONS;
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
		PrintWriter p = new PrintWriter(new File("examples/14/output.txt"));
		PrintWriter m = new PrintWriter(new File("examples/14/momenta.txt"));

	
		long start = System.currentTimeMillis();
		int nonce = 0;
		double temp;
		ArrayList<Double> memory = new ArrayList<Double>();
		double totalAvg = 0, currentMomentum = 0, avg = 0, totalTime = 0;
		for(int i=0;i<Constants.ITERATIONS; i++){
			currentMomentum = Spheres.getMomenta();
			avg			+= currentMomentum;
			totalAvg 	+= currentMomentum;
		
			
			memory.add(currentMomentum);
			if(memory.size()==1001){
				temp = memory.remove(0);
				avg -= temp;
				m.printf("%f %f\n", totalTime, avg/1000.0); //This is the print for the plot of momentum with time
			}
			current = Spheres.nextCollision();			
			Spheres.updatePositions(current.get(0).time);
			totalTime+=current.get(0).time;
			for(Collision c : current){
				c.process();
				p.println(c.toString()+" "+nonce);	 //This is the print for the animation
			}
//			if(!current.get(0).isBoundary()&&!current.get(0).isSwirl()){
//				Spheres.updateVelocities();
//			}
			nonce++;
		}
		
		
		
		long end = System.currentTimeMillis();
		System.out.printf("Simulation took %d milliseconds.\n\n\n",(end-start));
		totalAvg/=Constants.ITERATIONS;
		System.out.println("Momentum: "+totalAvg);

		m.close();
		p.close();

	}
	
	
	
	
	public static void setConstants(String[] args){
		Constants.MU = Double.parseDouble(args[0]);
		Constants.WMU = Double.parseDouble(args[1]);
		Constants.bound_vel = Double.parseDouble(args[2]);
		Constants.NUM_OF_SPHERES = Integer.parseInt(args[3]);
	}

	public static void initializeSpheres() throws FileNotFoundException{
		Scanner input = new Scanner(new File("examples/14/input.txt"));

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
			for(int j=Constants.DIMENSIONS+2;j<2*Constants.DIMENSIONS+2;j++){
				vel[j-Constants.DIMENSIONS-2] = Double.parseDouble(toParse[j]);
			}
			toAdd.vel = vel;
			Spheres.add(toAdd);
		}
		input.close();
	}
	
}
