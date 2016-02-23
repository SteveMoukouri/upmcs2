package algo;

import java.util.ArrayList;

public class NaiveMinCircle implements MinCircleSolver {
	
	public NaiveMinCircle() {
		super();
	}

	@Override
	public Circle getMinCircle(ArrayList<Point> points) {
		points = Tools.aklPreCalc(points);
		
		double diam = -1;
		if (points.size() <= 2) 
			return new Circle(new Point(-1, -1), -2);
		Circle c = null;
		for (Point p : points) {
			for (Point q: points) {
				if (p == q) break;
				Circle ctmp = new Circle(p, q);
				if (ctmp.containsAll(points)) {
					if (diam == -1 || diam > ctmp.getRadius()) {
						c = ctmp;
						diam = ctmp.getRadius();
					}
				}
			}
		}
		for (Point p : points) {
			for (Point q : points) {
				if (q == p) break;
				for (Point r : points) {
					if (r == q || r == p) {
						break;
					}
					try {
						Circle ctmp = new Circle(p, q, r);
						if (ctmp.containsAll(points)) {
							if (diam == -1 || diam > ctmp.getRadius()) {
								c = ctmp;
								diam = ctmp.getRadius();
							}
						}
					} catch (Exception e) { break; }
				}
			}
		}
		return c==null?new Circle(-1, -1, -1):c;
	}
}
