package simulation;


public class Packing {

	static int counter=0;
	public static void main(String[] args) {
		double[][] parr = new double[4][2];
		parr[0][0] = 0;
		parr[0][1] = 0;
		parr[1][0] = 2.01;
		parr[1][1] = 0;
		parr[2][0] = 1.005;
		parr[2][1] = 2;
		parr[3][0] = 3.015;
		parr[3][1] = 2;
		int counter=0;
		double x = 0;
		double y = -10;
		double salt;
		boolean evenrow = true;
		while(y<10){
			if(evenrow){
				x=-10;
				evenrow=false;
			}else{
				x=-8.95;
				evenrow=true;
			}
			while(x<10){
				if(check(x,y)){
					salt = Math.random()*0.001;
					System.out.printf("Pos %.5f %.5f Vel: 0 0\n", x+salt,y+salt);
					counter++;
				}
				x+=2.1;
			}
			y+=2;
		}System.out.println(counter);
		
		
	}
	
	public static boolean check(double x, double y){
		return x*x+y*y<80;
	}
}
