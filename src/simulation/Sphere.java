package simulation;

public class Sphere {
	int id;
	static int inc = 0;
	double[] pos;
	double[] vel;
	double theta;
	double theta_vel;
	public Sphere(){
		pos = new double[Constants.DIMENSIONS];
		vel = new double[Constants.DIMENSIONS];		
		id=inc++;
	}
	
	

}
