package algorithms;

import supportGUI.Circle;

public class WrapperCircle {
	private WrapperPoint center;
	private double radius;
	
	public WrapperCircle(WrapperPoint center, double radius) {
		super();
		this.center = center;
		this.radius = radius;
	}
	
	public WrapperPoint getCenter() {
		return center;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public boolean contains(WrapperPoint p) {
		return this.radius >= this.center.distance(p);
	}
	
	public Circle toCircle() {
		return new Circle(this.center.toPoint(), (int) this.radius);
	}
}
