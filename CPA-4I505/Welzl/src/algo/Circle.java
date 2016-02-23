package algo;

import java.util.ArrayList;

public class Circle {
	private Point center;
	private double radius;
	
	public Circle() { this.center = null; this.radius = -1; }
	
	public Circle(Point center, double radius) {
		this.center = center;
		this.radius = radius;
	}
	
	public Circle(int x, int y, double radius) {
		this.center = new Point(x, y);
		this.radius = radius;
	}
	
	public Circle(Point a, Point b) {
		this.center = new Point((a.getX()+b.getX())/2, (a.getY()+b.getY())/2);
		this.radius = Math.sqrt(a.distanceSq(b)) / 2;
	}
	
	// A REFAIRE
	public Circle(Point a, Point b, Point c) {
		double ux = (a.getX() + b.getX() + c.getX())/3;
		double uy = (a.getY() + b.getY() + c.getY())/3;
		this.center = new Point((int)(ux), (int)(uy));
		this.radius = Math.sqrt(a.distanceSq(this.center));
	}
	
	// A REFAIRE
	public static Circle computeBounds(ArrayList<Point> points) {
		if (points.size() > 3) {
			double uxs = 0, uys = 0;
			for (Point p : points) {
				uxs += p.getX();
				uys += p.getY();
			}
			return new Circle((int)uxs, (int)uys, Math.sqrt(points.get(0).distanceSq(points.get(1)))/2);
		}
		else if (points.size() == 3) {
			double ux = (points.get(0).getX() + points.get(1).getX() + points.get(2).getX())/3;
			double uy = (points.get(0).getY() + points.get(1).getY() + points.get(2).getY())/3;
			return new Circle((int)(ux), (int)(uy), Math.sqrt(points.get(0).distanceSq())));
		}
		else if (points.size() == 2){
			return new Circle(points.get(0), points.get(1));
		} else {
			return null;
		}
	}
	
	public boolean contains(Point p) {
		return center.distanceSq(p) <= (radius*radius);
	}
	
	public boolean containsAll(ArrayList<Point> points) {
		for (Point p : points)
			if (!contains(p))
				return false;
		return true;
	}
	
	public Point getCenter() { return center; }
	public double getRadius() { return radius; }
	
	public String toString() {
		return this.getCenter().toString() + " " + this.getRadius();
	}
}
