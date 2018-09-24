public class Planet {
	
	public double xxPos; // current x position 
	public double yyPos; // current y position 
	public double xxVel; // current velocity in the x direction 
	public double yyVel; // current velocity in the y direction 
	public double mass;  
	public String imgFileName;
	
	public Planet (double xP, double yP, double xV, double yV, double m, String img) {
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}
    /** take in a Planet object and initialize an identical Planet object  */
	public Planet (Planet p) {
		this (p.xxPos, p.yyPos, p.xxVel, p.yyVel, p.mass, p.imgFileName);
	}
    /** calculate the distance between two planets */
	public double calcDistance (Planet p) {
		double dx = this.xxPos - p.xxPos;
		double dy = this.yyPos - p.yyPos;
		return Math.sqrt (dx*dx + dy*dy);
	}
    /** takes in a planet, and returns a double describing the force exerted on this planet 
    by the given planet. F = G * m1 * m2 / r2 */
	public double calcForceExertedBy (Planet p) {
		double G = 6.67E-11;
		double r = calcDistance (p);
		return G * this.mass * p.mass/(r*r);
	}
    /** force exerted in the X directions */
	public double calcForceExertedByX (Planet p) {
		double F = calcForceExertedBy(p);
		double dx = p.xxPos - this.xxPos;
		double r = calcDistance(p);
		return F * dx / r;
	}
    /** force exerted in the Y directions */
	public double calcForceExertedByY (Planet p) {
		double F = calcForceExertedBy(p);
		double dy = p.yyPos - this.yyPos;
		double r = calcDistance(p);
		return F * dy / r;
	}
    /**take in an array of Planets and calculate the net X force exerted by all planets 
    in that array upon the current Planet.*/
	public double calcNetForceExertedByX (Planet[] allPlanets) {
		double FnetX = 0.0;
		for (Planet p : allPlanets) {
			if (this.equals(p) == false) {
				FnetX += calcForceExertedByX (p);
			}
		}
		return FnetX;
	}
    /** net Y force*/
	public double calcNetForceExertedByY (Planet [] allPlanets) {
		double FnetY = 0.0;
		for (Planet p : allPlanets) {
			if (this.equals(p) == false) {
				FnetY += calcForceExertedByY (p);
			}
		}
		return FnetY;
	}
    /** determines how much the forces exerted on the planet will cause that planet 
    to accelerate，and the resulting change in the planet's velocity and position 
    in a small period of time dt.　Acceleration: ax = Fx / m; ay = Fy / m*/
    public void update (double dt, double fX, double fY) {
    	double AnetX, AnetY;
    	AnetX = fX / mass;
    	AnetY = fY / mass;
    	xxVel += dt*AnetX;
    	yyVel += dt*AnetY;
    	xxPos += dt*xxVel;
    	yyPos += dt*yyVel;
    }

    public void draw() {
    	StdDraw.picture(xxPos, yyPos, "images/"+imgFileName); //draw the Planet's img at the Planet's position

    }

}