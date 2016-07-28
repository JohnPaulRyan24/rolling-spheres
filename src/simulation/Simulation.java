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
		
		for(int d = 10; d<=60; d++){
			Constants.NUM_OF_SPHERES = d;
			
			initializeSpheres();
			ArrayList<Collision> current;
			double totalAvg = 0;		
			double totalTime = 0;
			for(int i=0;i<Constants.ITERATIONS; i++){		
				totalAvg 	+= Spheres.getTheta();
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
		int testing = Integer.parseInt(args[6]);
		int hist = Integer.parseInt(args[7]);
		if(testing==1){
			testing(args);
			return;
		}
		boolean makeHistograms = false;
		if(hist==1){
			makeHistograms = true;
			return;
		}
		setConstants(args);
		
		initializeSpheres();
		
		PrintWriter p = new PrintWriter(new File("input/sim_data.txt"));
		
		PrintWriter hist_s = null, hist_w=null;
		if(makeHistograms) {
			hist_s = new PrintWriter(new File("hist_data_s.txt"));
			hist_w = new PrintWriter(new File("hist_data_w.txt"));
		}
		
		long start = System.currentTimeMillis();
		
		int nonce = 0;
		double totalAvg = 0, currentAngVel = 0,  totalTime = 0;
		
		ArrayList<Collision> currentCollision;
		currentAngVel = Spheres.getAngVel();
		double pastAngVel = currentAngVel;
		
		System.out.println("Initial Angular Velocity: "+currentAngVel+"<br>");

		for(int i=0;i<Constants.ITERATIONS; i++){
			totalAvg 	+= currentAngVel;
			
			currentCollision = Spheres.nextCollision();	
			totalTime+=currentCollision.get(0).time;
			Spheres.updatePositions(currentCollision.get(0).time, totalTime);
			
			for(Collision c : currentCollision){
				c.process();
				p.println(c.toString()+" "+nonce);	 //This is the print for the animation
			}
			currentAngVel = Spheres.getAngVel();
			nonce++;
			if(makeHistograms){
				if(!currentCollision.get(0).isSwirl()&&!currentCollision.get(0).isBoundary()){
					hist_s.println(currentAngVel-pastAngVel);
				}
				else if(currentCollision.get(0).isBoundary()){
					hist_w.println(currentAngVel-pastAngVel);
				}
			}
	
			pastAngVel = currentAngVel;
		
			
		}
		System.out.println("Final Angular Velocity: "+pastAngVel+"<br>");
		
		totalAvg/=Constants.ITERATIONS;
		System.out.println("Average Angular Velocity: "+totalAvg+"<br>");
		long end = System.currentTimeMillis();
		System.out.printf("Simulation took %d milliseconds.\n",(end-start));
		
		p.close();
		if(makeHistograms){
			hist_s.close();
			hist_w.close();
		}
	}
	
	
	
	
	public static void setConstants(String[] args) throws FileNotFoundException{
		Constants.MU = Double.parseDouble(args[0]);
		Constants.WMU = Double.parseDouble(args[1]);
		Constants.bound_vel = Double.parseDouble(args[2])/1000.0;
		Constants.NUM_OF_SPHERES = Integer.parseInt(args[3]);
		if(Integer.parseInt(args[4])>0){
			Constants.SPINNING = true;
		}else{
			Constants.SPINNING = false;
		}
		Constants.BOUNDRAD = Double.parseDouble(args[5]);
		
		
		File argFile = new File("args.txt");
		PrintWriter p = new PrintWriter(argFile);
		p.printf("%.9f\n%d\n%.9f\n%s\n_\n_\n_\n_\n", 1000*Constants.bound_vel,Constants.NUM_OF_SPHERES,Constants.BOUNDRAD,args[4]);
		
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
