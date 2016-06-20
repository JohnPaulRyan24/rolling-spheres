package simulation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Simulation {
	
	
	public static void main(String[] args) throws FileNotFoundException{
		
		setConstants(args);
		
		String path = "examples/"+args[0];
		initializeSpheres(path);
		
		ArrayList<Collision> current;
		PrintWriter p = new PrintWriter(new File(path+"/output.txt"));
		PrintWriter e = new PrintWriter(new File(path+"/energies.txt"));
		PrintWriter m = new PrintWriter(new File(path+"/cmomenta.txt"));

		long start = System.currentTimeMillis();
		int nonce = 0;
		
		double temp;
		double avg = 0, totalAvg = 0, totalTime = 0, currentMomentum = 0;
		ArrayList<Double> memory = new ArrayList<Double>();
		for(int i=0;i<Constants.ITERATIONS; i++){
			currentMomentum = Spheres.getMomenta();
			avg			+= currentMomentum;
			totalAvg 	+= currentMomentum;
			
			memory.add(currentMomentum);
			if(memory.size()==1001){
				temp = memory.remove(0);
				avg -= temp;
				m.printf("%f %f\n", totalTime, avg/1000.0);
			}
			
			e.printf("%f %f\n", totalTime, Spheres.getEnergy());
			
			current = Spheres.nextCollision();			
			Spheres.updatePositions(current.get(0).time);
			totalTime+=current.get(0).time;
			for(Collision c : current){
				c.process();
				p.println(c.toString()+" "+nonce);	
			}
//			if(!current.get(0).isBoundary()&&!current.get(0).isSwirl()){
//				Spheres.updateVelocities();
//			}
			nonce++;
		}
		
		totalAvg/=Constants.ITERATIONS;
		
		
		File tempfile = new File("cmomenta/"+args[5]+"/temp.txt");
		PrintWriter t = new PrintWriter(tempfile);
		t.write("Momentum: "+totalAvg+"\n");
		
		System.out.println("\n\n\nMomentum: "+totalAvg);
		
		
		long end = System.currentTimeMillis();
		System.out.printf("Simulation took %d milliseconds.\n\n\n",(end-start));
		
		t.close();
		p.close();
		e.close();
		m.close();
	}
	
	
	
	
	public static void setConstants(String[] args){
		Constants.MU = Double.parseDouble(args[1]);
		Constants.WMU = Double.parseDouble(args[2]);
		Constants.bound_vel = Double.parseDouble(args[3]);
		Constants.swirl_interval = Double.parseDouble(args[4]);
		Constants.NUM_OF_SPHERES = Integer.parseInt(args[6]);
	}

	public static void initializeSpheres(String path) throws FileNotFoundException{
		Scanner input = new Scanner(new File(path+"/input.txt"));

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
