package algorithms;

import java.awt.Point;

public class WrapperPoint {
	private double x;
	private double y;
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public WrapperPoint(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public double distance(WrapperPoint p) {
		return Math.sqrt((this.x - p.getX())*(this.x - p.getX()) 
				+ (this.y - p.getY())*(this.y - p.getY()));
	}
	
	public Point toPoint() {
		return new Point((int)this.x, (int)this.y);
	}
}
