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
		this.radius = a.distance(b) / 2;
	}
	
	private static boolean sameLine(Point a, Point b, Point c) {
		return (a.getX()*(b.getY()-c.getY())
				+b.getX()*(c.getY()-a.getY())
				+c.getX()*(a.getY()-b.getY()) == 0);
	}
	
	public static Circle circumCircle(Point a, Point b, Point c) {
		if (sameLine(a, b, c))
			return new Circle(0, 0, 0);
		double ux = (a.getX()*a.getX()+a.getY()*a.getY())*(b.getY()-c.getY())+
				(b.getX()*b.getX()+b.getY()*b.getY())*(c.getY()-a.getY())+
				(c.getX()*c.getX()+c.getY()*b.getY())*(a.getY()-b.getY());
		double uy = (a.getX()*a.getX()+a.getY()*a.getY())*(c.getX()-b.getX())+
				(b.getX()*b.getX()+b.getY()*b.getY())*(a.getX()-c.getX())+
				(c.getX()*c.getX()+c.getY()*b.getY())*(b.getX()-a.getX());
		double d = 2*(a.getX()*(b.getY()-c.getY())+
				b.getX()*(c.getY()-a.getY())+
				c.getX()*(a.getY()-b.getY()));
		Point p = new Point((int)(ux/d), (int)(uy/d));
		return new Circle(p, p.distance(b));
	}
	
	public static Circle computeBounds(ArrayList<Point> points) {
		if (points.size() == 3) {
			return circumCircle(points.get(0), points.get(1), points.get(2));
		}
		else if (points.size() == 2){
			return new Circle(points.get(0), points.get(1));
		} else {
			return new Circle(0, 0, 0);
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
