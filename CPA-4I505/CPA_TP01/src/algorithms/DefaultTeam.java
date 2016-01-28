package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/***************************************************************
 * TME 1: calcul de diamètre et de cercle couvrant minimum.    *
 *   - Trouver deux points les plus éloignés d'un ensemble de  *
 *     points donné en entrée.                                 *
 *   - Couvrir l'ensemble de poitns donné en entrée par un     *
 *     cercle de rayon minimum.                                *
 *                                                             *
 * class Circle:                                               *
 *   - Circle(Point c, int r) constructs a new circle          *
 *     centered at c with radius r.                            *
 *   - Point getCenter() returns the center point.             *
 *   - int getRadius() returns the circle radius.              *
 *                                                             *
 * class Line:                                                 *
 *   - Line(Point p, Point q) constructs a new line            *
 *     starting at p ending at q.                              *
 *   - Point getP() returns one of the two end points.         *
 *   - Point getQ() returns the other end point.               *
 ***************************************************************/
import supportGUI.Circle;
import supportGUI.Line;

public class DefaultTeam {

	// calculDiametre: ArrayList<Point> --> Line
	//   renvoie une pair de points de la liste, de distance maximum.
	public Line calculDiametre(ArrayList<Point> pts) {
		Random generator = new Random(System.currentTimeMillis());
		
		ArrayList<WrapperPoint> points = new ArrayList<WrapperPoint>();
		for (Point p : pts)
			points.add(new WrapperPoint(p.getX(), p.getY()));
		
		if (points.size()<3) {
			return null;
		}
		
		/*
		Point p=points.get(0);
		Point q=points.get(1);
		*/
		
		/**
		 * Algo naïf
		 */
		
		
		double diametre = 0;
		WrapperPoint p_acc = null;
		WrapperPoint q_acc = null;
		
		for (WrapperPoint p : points)
			for (WrapperPoint q : points) {
				double distance = p.distance(q);
				if (distance > diametre) {
					p_acc = p;
					q_acc = q;
					diametre = distance;
				}
			}
		return new Line(p_acc.toPoint(), q_acc.toPoint());
		
		
		/**
		 * Algo précalcul
		 */
		
		/**
		WrapperPoint p = popRandom(points, generator.nextInt());
		WrapperPoint q = popRandom(points, generator.nextInt());
		double distance = p.distance(q);
		while (points.size() > 0) {
			WrapperPoint x = popRandom(points, generator.nextInt());
			if (x.distance(p) > distance) {
				p = x;
				distance = x.distance(p);
			}
			if (x.distance(q) > distance) {
				q = x;
				distance = x.distance(q);
			} 
		}
		return new Line(p.toPoint(), q.toPoint());
		**/
	}
	
	// calculCercleMin: ArrayList<Point> --> Circle
	//   renvoie un cercle couvrant tout point de la liste, de rayon minimum.
	public Circle calculCercleMin(ArrayList<Point> pts) {
		ArrayList<WrapperPoint> points = new ArrayList<WrapperPoint>();
		for (Point p : pts)
			points.add(new WrapperPoint(p.getX(), p.getY()));
		if (points.isEmpty()) {
			return null;
		}

		/**
		 * Algo naïf 
		 *
		Point center=points.get(0);
		int radius=100;
		
		
		for (Point p : points) {
			for (Point q : points) {
				Circle c = new Circle(new Point((p.x + q.x)/2, (p.y+q.y)/2), radius(p, q));
				if (containsAll(points, c))
					System.out.println("lol");
					return c;
			}
		}
		for (Point p : points) {
			for (Point q : points) {
				for (Point r : points) {
					
				}
			}
		}
		
		*
		*/
		
		/**
		 * Algorithme de Ritter 
		 */
		
		Random generator = new Random(System.currentTimeMillis());
		
		/**
		 * Etapes aléatoires 
		 */
		/*
		WrapperPoint dummy = popRandom(points, generator.nextInt());
		WrapperPoint p = popFurthest(points, dummy);
		WrapperPoint q = popFurthest(points, p);
		*/
		
		/**
		 * Etapes naïves (diamètre)
		 */
		
		
		Line l = calculDiametre(pts);
		WrapperPoint p = new WrapperPoint(l.getP().getX(), l.getP().getY());
		WrapperPoint q = new WrapperPoint(l.getQ().getX(), l.getQ().getX());
		
		WrapperPoint c = new WrapperPoint((p.getX()+q.getX())/2,
				(p.getY()+q.getY())/2);
		WrapperCircle circle = new WrapperCircle(c, c.distance(p));
		popPoint(points, p);
		popPoint(points, q);
		while (points.size() > 0) {
			
			WrapperPoint s = chooseRandom(points, generator.nextInt());
			
			if (circle.contains(s))
				popPoint(points, s);
			else {
				double rad = (circle.getCenter().distance(s) + circle.getRadius())/2;
				double cx = circle.getCenter().getX();
				double cy = circle.getCenter().getY();
				double al = rad/circle.getCenter().distance(s);
				double be = (circle.getCenter().distance(s) - rad)
						/circle.getCenter().distance(s);
				double cxnew = al*cx + be*s.getX();
				double cynew = al*cy + be*s.getY(); 
				
				circle = new WrapperCircle(new WrapperPoint(cxnew, cynew), rad);
			}
		}
		return circle.toCircle();
	}
	
	private List<WrapperPoint> filtrageAKL(List<WrapperPoint> points) {
		/* Points extrêmes */ 
		WrapperPoint a, b, c, d;
		a = b = c = d = null;
		double ordmin, ordmax, absmin, absmax;
		ordmin = ordmax = absmin = absmax = 0;
		for (WrapperPoint p : points) {
			// Abs min
			if (a == null || p.getX() < absmin) {
				a = p;
				absmin = p.getX();
			}
			// Ord min
			if (b == null || p.getX() > absmax) {
				b = p;
				absmax = p.getX();
			}
			// Abs max
			if (c == null || p.getY() < ordmin) {
				c = p;
				ordmin = p.getY();
			}
			// Ord max
			if (d == null || p.getY() > ordmax) {
				d = p;
				ordmax = p.getY();
			}
		}
		// points = filtrageHeron(points, a, b, c, d);
		// points = filtrageVect(points, a, b, c, d);
		points = filtrageBary(points, a, b, c, d);
		return points; 
	}
	
	/* Ex8 q3 */
	private List<WrapperPoint> filtrageBary(List<WrapperPoint> points,
			WrapperPoint a, WrapperPoint b, WrapperPoint c, WrapperPoint d) {
		for (WrapperPoint p : points) {
			if (barycentres(a, b, c, p) || barycentres(a, c, d, p)) 
				popPoint(points, p);
		}
		return points;
	}
	
	private boolean barycentres(WrapperPoint a, WrapperPoint b, WrapperPoint c,
			WrapperPoint x) {
		double l1 = 
				((b.getY() - c.getY()) * (x.getX() - c.getX()) + 
						(c.getX() - b.getX()) * (x.getY() - c.getY())) 
						/ 
				((b.getY() - c.getY()) * (a.getX() - c.getX()) + 
						(c.getX() - b.getX()) * (a.getY() - c.getY()));
		double l2 =
				((c.getY() - a.getY()) * (x.getX() - c.getX()) + 
						(a.getX() - c.getX()) * (x.getY() - c.getY())) 
						/ 
				((b.getY() - c.getY()) * (a.getX() - c.getX()) + 
						(c.getX() - b.getX()) * (a.getY() - c.getY()));
		double l3 = 1 - l1 - l2;
		return ((l1 <= 1 && l2 <= 1 && l3 <= 1) && (l1 >= 0 && l2 >= 0 && l3 >= 0));
	}
	
	/* Ex8 q2 */
	private List<WrapperPoint> filtrageVect(List<WrapperPoint> points, 
			WrapperPoint a, WrapperPoint b, WrapperPoint c, WrapperPoint d) {
		/* Triangle ABC*/ 
		double abac = thirdCoord(a, b, a, c);
		double bcba = thirdCoord(b, c, b, a);
		double acab = thirdCoord(a, c, a, b);
		/* Triangle ACD */	
		double acad = thirdCoord(a, c, a, d);
		double cdca = thirdCoord(c, d, c, a);
		double adac = thirdCoord(a, d, a, c);
		for (WrapperPoint p : points) {
			/* Triangle ABC */ 
			double abap = thirdCoord(a, b, a, p);
			double bcbp = thirdCoord(b, c, b, p);
			double acap = thirdCoord(a, c, a, p);
			/* Triangle ACD */
			double cdcp = thirdCoord(c, d, c, p);
			double adap = thirdCoord(a, d, a, p);
			boolean isabc = sameSign(abac, abap) && sameSign(bcba, bcbp) &&
					sameSign(acab, acap);
			boolean isacd = sameSign(acad, acap) && sameSign(cdca, cdcp) &&
					sameSign(adac, adap);
			if (isabc || isacd) 
				popPoint(points, p);
		}
		return points;
	}
	
	private boolean sameSign(double a, double b) {
		return ((a >= 0 && b >= 0) || (a < 0 && b < 0));
	}
	
	private double thirdCoord(WrapperPoint a1, WrapperPoint a2, 
			WrapperPoint b1, WrapperPoint b2) {
		double ux = a2.getX() - a1.getX();
		double uy = a2.getY() - a1.getY();
		double vx = b2.getX() - b1.getY();
		double vy = b2.getY() - b1.getY();
		return (ux * vy) - (uy * vx);
	}
	
	/* Ex8 q1*/
	private List<WrapperPoint> filtrageHeron(List<WrapperPoint> points,
			WrapperPoint a, WrapperPoint b, WrapperPoint c, WrapperPoint d) {
		double aire2_abc = heron(a, b, c);
		double aire2_acd = heron(a, c, d); 
		for (WrapperPoint p : points) {
			double total = heron(a, b, p) + heron(b, c, p) + heron(a, c, p);
			if (aire2_abc == total || aire2_acd == total) {
				popPoint(points, p);
			}
		}
		return points;
	}
	
	private double heron(WrapperPoint a, WrapperPoint b, WrapperPoint c) {
		double ab = a.distance(b);
		double ac = a.distance(c);
		double bc = b.distance(c);
		double abc_s = (ab + ac + bc)/2; 
		return abc_s * (abc_s - ab) * (abc_s - ac) * (abc_s - bc);
	}
	
	private void popPoint(List<WrapperPoint> points, WrapperPoint p) {
		points.remove(p);
	}
	
	private WrapperPoint popRandom(List<WrapperPoint> points, int rnd) {
		WrapperPoint p = points.remove(Math.abs(rnd) % points.size());
		return p;
	}
	
	private WrapperPoint chooseRandom(List<WrapperPoint> points, int rnd) {
		return points.get(Math.abs(rnd) % points.size());
	}
	
	private WrapperPoint popFurthest(List<WrapperPoint> points, WrapperPoint c) {
		double d = 0;
		WrapperPoint f_acc = null;
		for (WrapperPoint p : points) {
			double dist = c.distance(p);
			if (d < dist) {
				f_acc = p;
			}
		}
		points.remove(f_acc);
		return f_acc;
	}
	
	private boolean containsAll(List<WrapperPoint> points, WrapperCircle c) {
		for (WrapperPoint p : points) {
			if (p.distance(c.getCenter()) <= c.getRadius())
				return false;
		}
		return true;
	}
}
