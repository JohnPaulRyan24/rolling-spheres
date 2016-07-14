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

	public void process(){
		double[] ZEROS = {0.0,0.0};
		if(isBoundary()){
			Sphere s = spheres[0];

			Vector boundvel = new Vector(Spheres.boundvel);
			double[] rad_arg = {s.pos[0]-Spheres.boundpos[0],s.pos[1]-Spheres.boundpos[1]};
			Vector rad = new Vector(rad_arg);
			double radNorm = rad.norm();
			rad = rad.times(1/radNorm); //rad is normal.
			
			
			Vector newVel = new Vector(s.vel);
			newVel = newVel.minus(boundvel);  //now we're in stationary boundary frame of reference

			Vector perp = rad.times(newVel.dot(rad));
			Vector par = newVel.minus(perp);
			perp = perp.times(-1);
			double damp = 2*Constants.WMU*perp.norm();
			double parnorm = par.norm();
			Vector normPar;
			if(Math.abs(parnorm)<1e-13){
				normPar=new Vector(ZEROS);
			}else{
				normPar = par.times(1.0/parnorm);
			}
			Vector newPar = par.minus(normPar.times(damp));
			if(newPar.dot(par)<0){
				double[] temp = {0,0};
				newPar = new Vector(temp);
			}
			perp = perp.add(newPar);
			perp = perp.add(boundvel);
			s.vel = perp.toArray();



		}else if(isSwirl()){
			double v0 = Spheres.boundvel[0];
			Spheres.boundvel[0] = Math.cos(SWIRL_ANGLE)*Spheres.boundvel[0]-Math.sin(SWIRL_ANGLE)*Spheres.boundvel[1];
			Spheres.boundvel[1] = Math.sin(SWIRL_ANGLE)*v0+Math.cos(SWIRL_ANGLE)*Spheres.boundvel[1];

		}


		else{	
			//PART 1: Initialize variables
			
			Sphere s1 = spheres[0];
			Sphere s2 = spheres[1];
			Vector v1 = new Vector(s1.vel);
			Vector v2 = new Vector(s2.vel);
			Vector x1 = new Vector(s1.pos);
			Vector x2 = new Vector(s2.pos);			
			Vector oldPerp1, unit;
			unit = x2.minus(x1).times(0.5); //1/2 because the norm is 2 no matter what		


			//PART 2: Calculate based on elastic collision
			oldPerp1 = unit.times(v1.dot(unit));
			double dot1 = v1.minus(v2).dot(x1.minus(x2));	
			dot1/=Math.pow(x1.minus(x2).norm(),2);
			Vector s1vel = v1.minus(x1.minus(x2).times(dot1));	
			double dot2 = v2.minus(v1).dot(x2.minus(x1));
			dot2/=Math.pow(x2.minus(x1).norm(),2);
			Vector s2vel = v2.minus(x2.minus(x1).times(dot2));


			//Part 3a - Break vectors into components
			Vector newPerp1 = unit.times(s1vel.dot(unit));
			Vector par1 = s1vel.minus(newPerp1);	
			if(Math.abs(par1.norm())<1e-13){
				par1 = new Vector(ZEROS);
			}
			unit = unit.times(-1);
			Vector newPerp2 = unit.times(s2vel.dot(unit));
			Vector par2 = s2vel.minus(newPerp2);	
			if(Math.abs(par2.norm())<1e-13){
				par2 = new Vector(ZEROS);
			}
			if(par1.norm()==0&&par2.norm()==0){
				s1.vel = newPerp1.toArray();
				s2.vel = newPerp2.toArray();
				return;
			}

			Vector delv1perp = newPerp1.minus(oldPerp1);
			double DELTA = delv1perp.norm();

			//Part 3b - r0 is x2vel minus x1vel
			double r0 = par1.norm() - par2.norm();
			int s0 = sign(r0);
			double t = r0/(2*Constants.MU*s0*newPerp1.minus(oldPerp1).norm());

			Vector newPar1, newPar2;


			//Part 3c - apply sticking rules
			//This can be written better - try just setting t=1 at some point. 
			t = Math.min(t, 1);
			Vector unitPar1, unitPar2;
			if(par1.norm()==0){
				unitPar2 = par2.times(1.0/par2.norm());
				unitPar1 = new Vector(unitPar2.toArray());
			}
			else if(par2.norm()==0){
				unitPar1 = par1.times(1.0/par1.norm());
				unitPar2 = new Vector(unitPar1.toArray());
			}
			else{
				unitPar1 = par1.times(1.0/par1.norm());
				unitPar2 = par2.times(1.0/par2.norm());
			}
			newPar1 = unitPar1.times(par1.norm()-Constants.MU*s0*DELTA*t);
			newPar2 = unitPar2.times(par2.norm()+Constants.MU*s0*DELTA*t);
			s1.vel = newPar1.add(newPerp1).toArray();
			s2.vel = newPar2.add(newPerp2).toArray();

		}	
	}	
	public int sign(double r0){
		if(r0>0) return 1;
		if(r0<0) return -1;
		return 0;
	}
	public String toString(){
		String toRet = "";
		if(swirl){
			toRet = "# # -1 "+String.format("%.9f %.9f %.9f %.9f %.9f", 
					time, Spheres.boundpos[0],Spheres.boundpos[1],Spheres.boundvel[0],Spheres.boundvel[1]);
			return toRet;
		}
		for(Sphere a:spheres){
			toRet+=String.format("## Id: %d posx: %.9f posy: %.9f velx: %.9f vely: %.9f theta: %.9f thetavel: %.9f ",
					a.id, a.pos[0] , a.pos[1],a.vel[0],a.vel[1], a.theta, a.theta_vel);

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
	public String toString(){
		return array[0]+" "+array[1];
	}
}

