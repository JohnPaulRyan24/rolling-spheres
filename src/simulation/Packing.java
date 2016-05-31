package simulation;

import java.util.ArrayList;

public class Packing {
	
	final static int RADIUS = 4;
	final static int ITERATIONS = 100;
	final static double PI = 3.141592653589793;
	static ArrayList<double[]> centers;
	public static void main(String[] args){
		centers = new ArrayList<double[]>();
		int i=0;
		double[] arr = new double[2];
		double r,t;
		while(i<ITERATIONS){
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
		for(double[] d : centers){
			System.out.printf("Pos: %f %f Vel: 0 0\n", d[0], d[1]);
			
		}System.out.println("SIZE: "+centers.size());
		
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
