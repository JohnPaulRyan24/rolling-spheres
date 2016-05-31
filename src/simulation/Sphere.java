package simulation;

public class Sphere {
	int id;
	static int inc = 0;
	double[] pos;
	double[] vel;
	public Sphere(){
		pos = new double[Constants.DIMENSIONS];
		vel = new double[Constants.DIMENSIONS];		
		id=inc++;
	}
	
	

}
