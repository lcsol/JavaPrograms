public class NBody {
	/** Given a file name, return a double corresponding to the radius of the universe 
	in that file*/
	public static double readRadius (String filename) {
		In in = new In (filename);
		int num = in.readInt();
	    double radius = in.readDouble();
	    return radius;
	}
	/** Given a file name, return an array of Planets corresponding to the planets 
	in the file*/
	public static Planet[] readPlanets (String filename) {
		In in = new In (filename);
		int num = in.readInt();
	    double radius = in.readDouble();
	    
	    Planet[] allplanets = new Planet[num];
	    
	    for (int i = 0; i < num; i++) {
	    	double xxPos = in.readDouble(); 
	        double yyPos = in.readDouble();
	        double xxVel = in.readDouble();  
	        double yyVel = in.readDouble();  
	        double mass = in.readDouble();  
	        String imgFileName = in.readString();

	        allplanets[i] = new Planet (xxPos, yyPos, xxVel, yyVel, mass, imgFileName);
	    }
	    return allplanets;   
	}

	public static void main (String[] args) {
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double radius = readRadius (filename);
		Planet[] allplanets = readPlanets (filename);

		StdDraw.setScale(-radius, radius);
		StdDraw.clear();
		StdDraw.picture(0, 0, "images/starfield.jpg"); //draw the image starfield.jpg as the background

		StdAudio.play("audio/2001.mid");

		for (Planet p : allplanets) {
			p.draw();
		}

		double time = 0.0;
		
		while (time <= T) {
			double [] xForces = new double[allplanets.length];
		    double [] yForces = new double[allplanets.length];

			for (int i = 0; i < allplanets.length; i++) {	
				xForces[i] = allplanets[i].calcNetForceExertedByX(allplanets);
				yForces[i] = allplanets[i].calcNetForceExertedByY(allplanets);
			}

			for (int i = 0; i < allplanets.length; i++) {
				allplanets[i].update(dt, xForces[i], yForces[i]);
			}
			
			StdDraw.clear();
		    StdDraw.picture(0, 0, "images/starfield.jpg");

		    for (Planet p : allplanets) {
				p.draw();
			}

			StdDraw.show(10);
			time += dt;
		}

	}
}