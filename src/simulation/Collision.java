package simulation;

public class Collision {
	static double PI = 3.141592653589793;
	static double SWIRL_ANGLE = (Constants.BOUND_TURN /180.0) * PI;
	double time;
	Sphere[] spheres;
	boolean swirl;
	public Collision(Sphere a, Sphere b, double t){
		swirl = false;
		time = t;
		spheres = new Sphere[2];
		spheres[0] = a;
		spheres[1] = b;
	}
	public Collision(Sphere a, double t){
		swirl = false;
		time = t;
		spheres = new Sphere[1];
		spheres[0] = a;
		
	}
	public Collision(double t){
		swirl = true;
		time = t;
	}
	public boolean isSwirl(){
		return swirl;
	}
	public boolean isBoundary(){
		if(swirl) return false;
		return spheres.length<2;
	}
	
	
	public void process(){//this only changes velocities
		if(isBoundary()){
			
			Sphere s = spheres[0];
			//vector is simply rotated about radius to point of contact, then negated
			double theta = Math.atan((s.pos[1]-Spheres.boundpos[1])/(s.pos[0]-Spheres.boundpos[0]));

			double cos = Math.cos(2*theta);
			double sin = Math.sin(2*theta);
			double tempv0 = cos*(s.vel[0]-Spheres.boundvel[0]) + sin*(s.vel[1]-Spheres.boundvel[1]);
			s.vel[1] = Spheres.boundvel[1]-sin*(s.vel[0]-Spheres.boundvel[0]) + cos*(s.vel[1]-Spheres.boundvel[1]);
			s.vel[0] = Spheres.boundvel[0]-tempv0;	
			
			
			
			
			
		}else if(isSwirl()){
			double v0 = Spheres.boundvel[0];
			Spheres.boundvel[0] = Math.cos(SWIRL_ANGLE)*Spheres.boundvel[0]-Math.sin(SWIRL_ANGLE)*Spheres.boundvel[1];
			Spheres.boundvel[1] = Math.sin(SWIRL_ANGLE)*v0+Math.cos(SWIRL_ANGLE)*Spheres.boundvel[1];
			
		}
		else{
			//we need to change the velocities according to the formula from wiki
			Sphere s1 = spheres[0];
			Sphere s2 = spheres[1];
			Vector v1 = new Vector(s1.vel);
			Vector v2 = new Vector(s2.vel);
			Vector x1 = new Vector(s1.pos);
			Vector x2 = new Vector(s2.pos);
			double dot1 = v1.minus(v2).dot(x1.minus(x2));
			
			dot1/=Math.pow(x1.minus(x2).norm(),2);
			
			s1.vel = v1.minus(x1.minus(x2).times(dot1)).toArray();	
			
			
			double dot2 = v2.minus(v1).dot(x2.minus(x1));
			dot2/=Math.pow(x2.minus(x1).norm(),2);
			s2.vel = v2.minus(x2.minus(x1).times(dot2)).toArray();
			
		}
	}
	
	public String toString(){
		String toRet = "";
		if(swirl){
			toRet = "# # -1 "+String.format("%.9f %.9f %.9f %.9f %.9f", time, Spheres.boundpos[0],Spheres.boundpos[1],Spheres.boundvel[0],Spheres.boundvel[1]);
			return toRet;
		}
		for(Sphere a:spheres){
			toRet+=String.format("## Id: %d posx: %.9f posy: %.9f velx: %.9f vely: %.9f ",a.id, a.pos[0] , a.pos[1],a.vel[0],a.vel[1]);
			
		}
		toRet+=String.format("time: %.9f", time);
		return toRet;
	}
	
}


class Vector{
	double[] array;
	public Vector(double[] arr){
		array=arr;
	}
	public double norm(){
		return Math.sqrt(this.dot(this));
	}
	public double dot(Vector other){
		double toRet = 0;
		for(int i=0;i<array.length;i++){
			toRet+= array[i]*other.array[i];
		}return toRet;
	}
	public Vector add(Vector other){
		double[] toRet = new double[array.length];
		for(int i=0;i<toRet.length;i++){
			toRet[i] = array[i]+other.array[i];
		}return new Vector(toRet);		
	}
	public Vector minus(Vector other){
		double[] toRet = new double[array.length];
		for(int i=0;i<toRet.length;i++){
			toRet[i] = array[i]-other.array[i];
		}return new Vector(toRet);		
	}
	public Vector times(double c){
		double[] toRet = new double[array.length];
		for(int i=0;i<toRet.length;i++){
			toRet[i] = array[i]*c;
		}return new Vector(toRet);		
	}
	public double[] toArray(){
		return array;
	}
}
