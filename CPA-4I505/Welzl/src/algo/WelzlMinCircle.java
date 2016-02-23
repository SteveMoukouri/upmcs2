package algo;

import java.util.ArrayList;
import java.util.Random;

public class WelzlMinCircle implements MinCircleSolver {
	
	private Random generator;
	
	public WelzlMinCircle() {
		this.generator = new Random(System.currentTimeMillis());
	}
	
	/*
	b_minidisk(P, R):
		if P == null [or |R| == 3]:
			D = b_md(null, R)
		else:
			choose random p in P
			D = b_minidisk(P-{p}, R)
			if [D defined and] !p in D:
			 	D = b_minidisk(P - {p}, R u {p})
		return D
	 */

	@Override
	public Circle getMinCircle(ArrayList<Point> points) {
		points = Tools.aklPreCalc(points);
		Circle c = boundingMinDisk(points, new ArrayList<Point>());
		return c;
	}
	
	private Circle boundingMinDisk(ArrayList<Point> points, ArrayList<Point> border) {
		Circle c = null;
		if (points.size() == 0 || border.size() == 3) {
			c = Circle.computeBounds(border);
		}
		else {
			Point rnd = randomPoint(points);
			points.remove(rnd);
			c = boundingMinDisk(points, border);
			if (!c.contains(rnd)) {//(c!=null) && (!c.contains(rnd))) {
				border.add(rnd);
				c = boundingMinDisk(points, border);
			}
		}
		return c;
	}
	
	private Point randomPoint(ArrayList<Point> points) {
		int rnd = this.generator.nextInt(points.size());
		return points.get(rnd);
	}
}