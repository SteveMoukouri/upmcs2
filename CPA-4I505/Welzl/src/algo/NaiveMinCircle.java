package algo;

import java.util.ArrayList;

public class NaiveMinCircle implements MinCircleSolver {
	
	public NaiveMinCircle() {
		super();
	}

	@Override
	public Circle getMinCircle(ArrayList<Point> points) {
		// points = Tools.aklPreCalc(points);
		
		double diam = Double.POSITIVE_INFINITY;
		Circle c = null;
		if (points.size() <= 2) 
			return new Circle(new Point(-1, -1), -1);
		
		for (Point p : points) {
			for (Point q: points) {
				if (p == q) continue;
				Circle ctmp = new Circle(p, q);
				if (ctmp.containsAll(points)) {
					if (diam > ctmp.getRadius()) {
						c = ctmp;
						diam = ctmp.getRadius(); 
					}
				}
			}
		}
		
		if (c != null)
			return c;
		
		for (Point p : points) {
			for (Point q : points) {
				if (q == p) continue;
				for (Point r : points) {
					if (r == q || r == p) continue;
					try {
						Circle ctmp = Circle.circumCircle(p, q, r);
						if (ctmp.containsAll(points)) {
							if (diam > ctmp.getRadius()) {
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
