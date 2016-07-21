package simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ShrinkBoundary {
	static double RADIUS;
	
	
	public static void main(String[] args) throws FileNotFoundException{

		RADIUS = Double.parseDouble(args[0]);

		File in = new File("input/rad10.txt");
		Scanner input = new Scanner(in);
		PrintWriter output = new PrintWriter("input/input.txt");
		String line;
		int counter=0;
		output.println(input.nextLine());
		while(input.hasNextLine()){
			line = input.nextLine();
			if(line.length()==0) break;
			if(check(line)){
				counter++;
				output.println(line);
			}
		}
		System.out.println("New input file has "+counter+" discs.");
		input.close();
		output.close();
		
	}
	public static boolean check(String line){
		if(RADIUS==10){
			return true;
		}
		String[] arr = line.split(" ");
		double x = Double.parseDouble(arr[1]);
		double y = Double.parseDouble(arr[2]);
		if(Math.pow(RADIUS-1,2)-Math.pow(x, 2)-Math.pow(y,2) >1e-12){
			return true;
		}return false;
		
		
		
	}
}
