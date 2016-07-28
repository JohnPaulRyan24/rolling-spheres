package simulation;

import java.util.ArrayList;



public class Spheres {
	static Sphere[] Spheres;
	static double[] boundpos;
	static double[] boundvel;
	static double M_ANG;
	static double swirlTime;
	static int index;
	static double swirl_interval = Constants.swirl_interval;
	
	public static void init(){
		Spheres = new Sphere[Constants.NUM_OF_SPHERES];
		index = 0;
		boundpos = new double[2];
		boundvel = new double[2];
		boundpos[0]=0;
		boundpos[1]=0;
		boundvel[0]=Constants.bound_vel;
		boundvel[1]=0;
		swirlTime=0;
		M_ANG = 3*Constants.PI/2;
	}
	public static void add(Sphere a){
		Spheres[index++] = a;
	}
	public static Collision nextCollision(Sphere a, Sphere b){ 


		double dv0 = a.vel[0]-b.vel[0];
		double dv1 = a.vel[1]-b.vel[1];
		double dp0 = a.pos[0]-b.pos[0];
		double dp1 = a.pos[1]-b.pos[1];

		double A = Math.pow(dv0,2)+Math.pow(dv1,2);
		double B = 2*( dp0* dv0 + dp1 *dv1);
		double C = Math.pow(dp0,2)+Math.pow(dp1,2)-4;//ball radius
		double disc = B*B-4*A*C;
		if(disc<0){
			return new Collision(a,b,-1);
		}
		//now we know it's real
		double toRet = (0.0-B-Math.sqrt(disc))/(2*A);
		if(toRet>2e-13){
			return new Collision(a,b,toRet);
		}
		return new Collision(a,b,-1);

	}
	public static Collision nextWallCollision(Sphere a){

		double dv0 = a.vel[0]-boundvel[0];
		double dv1 = a.vel[1]-boundvel[1];
		double dp0 = a.pos[0]-boundpos[0];
		double dp1 = a.pos[1]-boundpos[1];

		double A = Math.pow(dv0,2)+Math.pow(dv1,2);
		double B = 2*( dp0* dv0 + dp1 *dv1);
		double C = Math.pow(dp0,2)+Math.pow(dp1,2)-Math.pow(Constants.BOUNDRAD-1, 2);
		double disc = B*B-4*A*C;
		if(disc<0){
			return new Collision(a,-1);
		}
		double toRet = (0.0-B+Math.sqrt(disc))/(2*A);
		if(toRet>2e-13){
			return new Collision(a,toRet);
		}
		return new Collision(a,-1);

	}
	public static void updatePositions(double time, double totalTime){
		M_ANG = (-Constants.PI/2)+Constants.PI*totalTime/6000.0;
		if(M_ANG>2*Constants.PI){
			M_ANG-=2*Constants.PI;
		}
		Sphere sphere;
		for(int i=0;i<Spheres.length;i++){
			sphere = Spheres[i];
			sphere.pos[0] = sphere.pos[0]+time*sphere.vel[0];
			sphere.pos[1] = sphere.pos[1]+time*sphere.vel[1];
			sphere.theta += time*sphere.theta_vel;
		}
		boundpos[0]+=time*boundvel[0];
		boundpos[1]+=time*boundvel[1];

	}
	public static ArrayList<Collision> nextCollision(){ //returns the two spheres 
		ArrayList<Collision> toRet = new ArrayList<Collision>();
		
		checkSphereCollisions(toRet);
		checkWallCollisions(toRet);
		if(Constants.bound_vel!=0) checkSwirlCollision(toRet);

		return toRet;
	}
	public static void checkSphereCollisions(ArrayList<Collision> toRet){
		Sphere a,b;
		Collision col;
		for(int i=0;i<Spheres.length;i++){

			a = Spheres[i];
			for(int j=i+1;j<Spheres.length; j++){
				b = Spheres[j];

				col = nextCollision(a,b);

				if(col.time!=-1){
					if(toRet.size()==0){
						toRet.add(col);
					}else if(toRet.get(0).time-col.time>=2e-13){
						//*SEE NOTE IN NEXT ELSEIF BLOCK

						for(int k=0;k<toRet.size();){ //THE REASON FOR THIS FOR LOOP IS THAT WE USE get(0) AS THE BASE
							Collision c = toRet.get(k);
							if(Math.abs(c.time-col.time)>2e-13){
								//THIS FOR LOOP HAS CLEARED toRet FOR ALL CASES OTHER THAN SAME TIME COLLISIONS
								toRet.remove(k);
							}else{
								//System.out.println("HELLO");
								k++;
							}
						}
						toRet.add(col);
					}
					else if(Math.abs(toRet.get(0).time-col.time)<=2e-13){  
						//*IMPORTANT NOTE - THIS HAS ONLY BEEN FOUND TO HAPPEN FOR EXACT SAME TIME COLLISIONS
						//System.out.println("SAME TIME");
						toRet.add(col);
					}
				}
			}
			


		}
		
	}
	public static void checkWallCollisions(ArrayList<Collision> toRet){
		Collision col;
		for(Sphere a: Spheres){
			col = nextWallCollision(a);
			
			if(col.time!=-1){
				if(toRet.size()==0){
					toRet.add(col);
					
				}else if(toRet.get(0).time-col.time>=2e-13){
					//See *comments for spheres for loop above
					for(int j=0;j<toRet.size();){
						Collision c = toRet.get(j);
						if(Math.abs(c.time-col.time)>2e-13){
							toRet.remove(j);
						}else{
							j++;
						}
					}

					toRet.add(col);
				}
				else if(Math.abs(toRet.get(0).time-col.time)<=2e-13){ 
					toRet.add(col);
				}
			}
		}
		
		
	}
	public static void checkSwirlCollision(ArrayList<Collision> toRet){
		double swirlHit = swirl_interval-swirlTime;
		if(toRet.size()==0){
			toRet.add(new Collision(swirlHit));
			swirlTime=0;
		}
		else if(toRet.get(0).time-swirlHit>=2e-13){
			for(int j=0;j<toRet.size();){
				Collision c = toRet.get(j);
				if(Math.abs(c.time-swirlHit)>2e-13){
					toRet.remove(j);
				}else{

					j++;
				}
			}
			toRet.add(new Collision(swirlHit));
			swirlTime=0;
		}
		else if(Math.abs(toRet.get(0).time-swirlHit)<=2e-13){ 
			//SEE * comments above
			
			toRet.add(new Collision(swirlHit));
			swirlTime=0;
		}
		else{
			
			swirlTime+=toRet.get(0).time;
		}
	}
	public static double getEnergy(){
		double toRet = 0;
		for(Sphere s : Spheres){
			toRet += Math.pow(s.vel[0], 2)+Math.pow(s.vel[1], 2);	
		}return toRet;
	}
	public static double[] centerOfMass(){
		double[] toRet = new double[2];
		toRet[0]=0.0;
		toRet[1]=0.0;
		for(Sphere s: Spheres){
			toRet[0]+=s.pos[0];
			toRet[1]+=s.pos[1];
		}
		toRet[0]/=Constants.NUM_OF_SPHERES;
		toRet[1]/=Constants.NUM_OF_SPHERES;
		return toRet;
	}
	public static double getTheta(){
	
		double[] com =centerOfMass();
		double[] ang1 = new double[2];
		double[] ang2 = new double[2];
		ang2[0] = Math.cos(M_ANG);
		ang2[1] = Math.sin(M_ANG);
		for(int i=0;i<2;i++){
			ang1[i] = com[i] - boundpos[i];
		}
		double norm = norm(ang1);
		if(norm==0) return 180;
		double dot = (ang1[0]*ang2[0]+ang1[1]*ang2[1])/norm;
		return Constants.rad2deg*Math.acos(dot);
		
	}
	public static double getTemperature(){
		double total = 0;
		double[] vel = new double[2];
		double temp;
		for(Sphere s: Spheres){
			vel[0] = s.vel[0]-boundvel[0];
			vel[1] = s.vel[1]-boundvel[1];	
			temp = 1000*norm(vel);
			total+= Math.pow(temp, 2);	
		}
		return total;
	}
	public static double getFreqVariance(){
		double f_bar = realGetAngVel();
		double sum = squareSum();
		double[] radius = new double[2];
		double[] center = centerOfMass();
		double f, p, norm;
		double toRet = 0;
		for(Sphere s: Spheres){
			radius[0] = s.pos[0] - center[0];
			radius[1] = s.pos[1] - center[1];
			norm = norm(radius);
			f = radius[0]*s.vel[1] - radius[1]*s.vel[0];
			f*=1000; //because we scaled time
			f /= Math.pow(norm, 2);
			p = Math.pow(norm,2)/sum;
			toRet += Math.pow(f, 2)*p;	
		}
		return toRet - Math.pow(f_bar,2);
	}
	public static double getAngVel(){
		return realGetAngVel();//realGetAngVel();
		//return getTheta();
	}
	public static double getDensity(double panc_rad){
		double[] center  = centerOfMass();
		double[] dist = new double[2];
		double toRet = 0;
		for(Sphere s : Spheres){
			dist[0] = s.pos[0]-center[0];
			dist[1] = s.pos[1]-center[1];
			if(norm(dist)>panc_rad){
				toRet++;
			}
		}
		return toRet;
	}
	public static double getDist2Center(){
		double[] com = centerOfMass();
		double[] dist = new double[2];
		dist[0] = com[0]-boundpos[0];
		dist[1] = com[1]-boundpos[1];
		return norm(dist);
	}
	public static double squareSum(){
		double toRet =0;
		double[] radius = new double[2];
		double[] center = centerOfMass();
		for(Sphere s: Spheres){
			radius[0] = s.pos[0] - center[0];
			radius[1] = s.pos[1] -center[1];
			toRet+= Math.pow(norm(radius),2);
		}return toRet;
	}
	public static double realGetAngVel(){
		double[] center = centerOfMass();
		double[] radius = new double[2];

		double sum = squareSum();
		double num=0;
		for(Sphere s: Spheres){
			radius[0] = s.pos[0]-center[0];
			radius[1] = s.pos[1]-center[1];
			num+=(radius[0]*s.vel[1]-radius[1]*s.vel[0]);
			
		}
		return (1000*num)/sum;//multiply by 1000 because we scaled time. 
		
	}
	public static double distance(Sphere a, Sphere b){
		double dist = 0;
		dist+=Math.pow(a.pos[0]-b.pos[0],2);
		dist+=Math.pow(a.pos[1]-b.pos[1],2);
		return Math.sqrt(dist);
	}
	public static double distance(Sphere a){
		double dist = 0;
		dist+=Math.pow(a.pos[0]-boundpos[0],2);
		dist+=Math.pow(a.pos[1]-boundpos[1],2);
		return 5-Math.sqrt(dist);
	}
	public static double norm(double[] vel){
		double toRet=0;
		for(double i : vel){
			toRet+=Math.pow(i, 2);
		}
		return Math.sqrt(toRet);
	}

}

