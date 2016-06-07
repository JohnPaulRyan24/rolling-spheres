package simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Packing {

	final static int RADIUS = 5;
	final static int ITERATIONS = 100;
	final static double PI = 3.141592653589793;
	static ArrayList<double[]> centers;
	static int DIR_NUM = 2;
	public static void main(String[] args) throws FileNotFoundException{
		for(int j=14;j<15;j++){
			DIR_NUM=j;

			centers = new ArrayList<double[]>();
			int i=0;
			double[] arr = new double[2];
			double r,t;
			while(i<ITERATIONS&&centers.size()<DIR_NUM){
				r = Math.random()*(RADIUS-1);
				t = Math.random()*2*PI;
				arr[0] = r*Math.cos(t);
				arr[1] = r*Math.sin(t);
				if(check(arr)){
					centers.add(arr);
					i=0;
				}else{
					i++;
				}
				arr = new double[2];// not sure if this is necessary
			}
			File dir = new File("examples/"+DIR_NUM);
			if(!dir.exists()){
				dir.mkdir();
			}
			System.out.println(centers.size());
			PrintWriter p = new PrintWriter(new File("examples/"+DIR_NUM+"/input.txt"));
			p.println("Change the numbers below as necessary. Radius of boundary is 10. Radius of spheres is 1");
			p.println("Number_Of_Spheres:");
			p.println(centers.size());
			p.println("Initial positions/velocities:");
			for(double[] d : centers){
				p.printf("Pos: %f %f Vel: %f %f\n", d[0], d[1], Math.random(), Math.random());

			}
			p.close();
		}
	}
	public static boolean check(double[] point){
		for(double[] center: centers){
			if(dist(point, center)<=2.1){
				return false;
			}
		}return true;
	}
	public static double dist(double[] point1, double[] point2){
		double sum = 0;
		for(int i=0;i<point1.length;i++){
			sum += Math.pow(point1[i]-point2[i], 2);
		}return Math.sqrt(sum);
	}

}
