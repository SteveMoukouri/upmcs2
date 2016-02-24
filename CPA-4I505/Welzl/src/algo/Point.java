package algo;

public class Point {
	private int x;
	private int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the point in the middle of [AB]
	 * @param a
	 * @param b
	 */
	public Point(Point a, Point b) {
		this.x = (a.getX() + b.getX()) / 2;
		this.y = (a.getY() + b.getY()) / 2;
	}
	
	public double distanceSq(Point p) {
		double pxmtx = (p.getX()-this.getX());
		double pymty = (p.getY()-this.getY());
		return (pxmtx*pxmtx) + (pymty*pymty);
	}
	
	public double distance(Point p) {
		return Math.sqrt(this.distanceSq(p));
	}

	public int getX() { return x; }
	public int getY() { return y; }
	
	public String toString() {
		return this.getX() + " " + this.getY();
	}
}
