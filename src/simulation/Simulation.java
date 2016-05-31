package simulation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Simulation {
	
	public static void main(String[] args) throws FileNotFoundException{
		Constants.MU = Double.parseDouble(args[1]);
		String path = "examples/"+args[0];
		Scanner input = new Scanner(new File(path+"/input.txt"));
		input.nextLine();input.nextLine();
		
		
//		PART 1 - Parse the input file
		Constants.NUM_OF_SPHERES = Integer.parseInt(input.nextLine());
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
		//END PART 1
		
		
		
		
		ArrayList<Collision> current;
		//PART 2 - Process Collisions - 10,000 iterations
		PrintWriter p = new PrintWriter(new File(path+"/output.txt"));
		PrintWriter e = new PrintWriter(new File(path+"/energies.txt"));
		int nonce = 0;
		double totalTime = 0;
		for(int i=0;i<Constants.ITERATIONS; i++){
			e.printf("%f %f\n", totalTime, Spheres.getEnergy());
			
			current = Spheres.nextCollision();			
			Spheres.updatePositions(current.get(0).time);
			totalTime+=current.get(0).time;
			for(Collision c : current){
				c.process();
				p.println(c.toString()+" "+nonce);	
			}
			if(!current.get(0).isBoundary()&&!current.get(0).isSwirl()){
				Spheres.updateVelocities();
			}
			nonce++;
		}
		//Create set of spheres with initial pos/vel
		//Create boundary with initial pos/vel
		p.close();
		e.close();
		input.close();
	}
}
