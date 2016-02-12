package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

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

    Random generator = new Random();

	// calculDiametre: ArrayList<Point> --> Line
	//   renvoie une pair de points de la liste, de distance maximum.
	public Line calculDiametre(ArrayList<Point> pts) {
		
		ArrayList<WrapperPoint> points = new ArrayList<WrapperPoint>();
		for (Point p : pts)
			points.add(new WrapperPoint(p.getX(), p.getY()));
		
		if (points.size()<3) {
			return null;
		}

		return diametreNaif(points);
	}
	
	public Line diametrePrecalc(ArrayList<WrapperPoint> points) {
		WrapperPoint p = popRandom(points);
		WrapperPoint q = popRandom(points);
		double distance = p.distance(q);
		while (points.size() > 0) {
			WrapperPoint x = popRandom(points);
			if (x.distance(p) > distance) {
				p = x;
				distance = x.distance(p);
			}
			if (x.distance(q) > distance) {
				q = x;
				distance = x.distance(q);
			} 
		}
		return new Line(p.toPoint(), q.toPoint());
	}
	
	public Line diametreNaif(ArrayList<WrapperPoint> points) {
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
	}
	
	// calculCercleMin: ArrayList<Point> --> Circle
	//   renvoie un cercle couvrant tout point de la liste, de rayon minimum.
	public Circle calculCercleMin(ArrayList<Point> pts) {
		if (pts.isEmpty()) {
			return null;
		}
		ArrayList<WrapperPoint> points = new ArrayList<>();
		for (Point p : pts)
			points.add(new WrapperPoint(p.getX(), p.getY()));
		return randomRitter(points);
	}
	
	/**
	 * Ritter initialisé par calcul du diamètre
	 * @param pts La liste de points
	 * @return Une approximation du cercle minimum.
	 */
	public Circle diametreRitter(ArrayList<Point> pts) {
		Line l = calculDiametre(pts);
		ArrayList<WrapperPoint> points = new ArrayList<WrapperPoint>();
		for (Point p : pts)
			points.add(new WrapperPoint(p.getX(), p.getY()));
		WrapperPoint p = new WrapperPoint(l.getP().getX(), l.getP().getY());
		WrapperPoint q = new WrapperPoint(l.getQ().getX(), l.getQ().getX());
		
		WrapperPoint c = new WrapperPoint((p.getX()+q.getX())/2,
				(p.getY()+q.getY())/2);
		WrapperCircle circle = new WrapperCircle(c, c.distance(p));
		popPoint(points, p);
		popPoint(points, q);
		while (points.size() > 0) {
			
			WrapperPoint s = chooseRandom(points);
			
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

	/**
	 * Ritter avec initialisation aléatoire
	 * @param points La liste de points
	 * @return Une approximation du cercle minimum
	 */
	public Circle randomRitter(ArrayList<WrapperPoint> points) {
		WrapperPoint dummy = popRandom(points);
		WrapperPoint p = popFurthest(points, dummy);
		WrapperPoint q = popFurthest(points, p);
		WrapperPoint c = new WrapperPoint((p.getX()+q.getX())/2,
				(p.getY()+q.getY())/2);
		WrapperCircle circle = new WrapperCircle(c, c.distance(p));
		popPoint(points, p);
		popPoint(points, q);
		while (points.size() > 0) {
			
			WrapperPoint s = chooseRandom(points);
			
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
	
	/**
	 * Ne pas utiliser
	 * @param points La liste de points
	 * @return Le cercle minimum exact.
	 */
	public Circle cercleNaif(ArrayList<WrapperPoint> points) { return null; } 
	
	/**
	 * Ritter aléatoire avec filtrage AKL naif
	 * @param points
	 * @return
	 */
	public Circle aklNaif(ArrayList<WrapperPoint> points) {
		WrapperPoint a = null, b = null, c = null, d = null;
		for (WrapperPoint p : points) { 
			if (a == null || p.getX() < a.getX())
				a = p;
			if (b == null || p.getY() < b.getY())
				b = p;
			if (c == null || p.getX() > c.getX())
				c = p;
			if (d == null || p.getY() > d.getY())
				d = p;
		}
		for (WrapperPoint x: points) {
			double ab = a.distance(b), ac = a.distance(c), ad = a.distance(d);
			double bc = b.distance(c), cd = c.distance(d);
			double s1 = (ab + bc + ac) / 2, s2 = (ac + cd + ad) / 2;
			double ax = a.distance(x), bx = b.distance(x), cx = c.distance(x), dx = d.distance(x);
			double totsq1 = trAreaSq(s1, ab, bc, ac);
			double totsq2 = s2 * (s2 - ac) * (s2 - cd) * (s2 - ad);
			double s1x1 = (ab + bx + ax) / 2, s1x2 = (bc + cx + bx) / 2, s1x3 = (ac + cx + ax) / 2;
			double s2x1 = (ac + ax + cx) / 2, s2x2 = (cd + dx + cx) / 2, s2x3 = (1) / 2;
			double sum1 = trAreaSq(s1x1, ab, bx, ax) + trAreaSq(s1x2, bc, bx, cx) + trAreaSq(s1x3, ac, cx, ax);
			double sum2 = trAreaSq(s2x1, ac, ax, cx) + trAreaSq(s2x2, cd, dx, cx) + trAreaSq(s2x3, ad, dx, ax);
			if (sum1 == totsq1 || sum2 == totsq2) {
				popPoint(points, x);
			}
		}
		return randomRitter(points);
	}
	
	/**
	 * Ritter aléatoire avec filtrage akl barycentrique
	 * @param points
	 * @return
	 */
	public Circle aklBary(ArrayList<WrapperPoint> points) {
		WrapperPoint a = null, b = null, c = null, d = null;
		for (WrapperPoint p : points) { 
			if (a == null || p.getX() < a.getX())
				a = p;
			if (b == null || p.getY() < b.getY())
				b = p;
			if (c == null || p.getX() > c.getX())
				c = p;
			if (d == null || p.getY() > d.getY())
				d = p;
		}
		for (WrapperPoint x: points) {
			// l1 = ((B.y − C.y) ∗ (X.x − C.x) + (C.x − B.x) ∗ (X.y − C.y))/((B.y − C.y) ∗ (A.x − C.x) + (C.x − B.x) ∗ (A.y − C.y))
			double l11 = ((b.getY() - c.getY()) * (x.getX() - c.getX()) + (c.getX() - b.getX()) * (x.getY() - c.getY()))/
					((b.getY() - c.getY()) * (a.getX() - c.getX()) + (c.getX() - b.getX()) * (a.getY() - c.getY()));
			double l12 = ((c.getY() - a.getY()) * (x.getX() - c.getX()) + (a.getX() - c.getX()) * (x.getY() - c.getY()))/
					((b.getY() - c.getY()) * (a.getX() - c.getX()) + (c.getX() - b.getX()) * (a.getY() - c.getY()));
			double l13 = 1 - l11 - l12;
			double l21 = ((c.getY() - d.getY()) * (x.getX() - d.getX()) + (d.getX() - c.getX()) * (x.getY() - d.getY()))/
					((c.getY() - d.getY()) * (a.getX() - d.getX()) + (d.getX() - c.getX()) * (a.getY() - d.getY()));
			double l22 = ((d.getY() - a.getY()) * (x.getX() - d.getX()) + (a.getX() - d.getX()) * (x.getY() - d.getY()))/
					((c.getY() - d.getY()) * (a.getX() - d.getX()) + (d.getX() - c.getX()) * (a.getY() - d.getY()));
			double l23 = 1 - l21 - l22;
			if (zeroToOne(l11, l12, l13) || zeroToOne(l21, l22, l23))
				popPoint(points, x);
		}
		return randomRitter(points);
	}
	
	private boolean zeroToOne(double a, double b, double c) {
		return (a >= 0 && b >= 0 && c >= 0 && a <= 1 && b <= 1 && c <= 1);
	}
	
	private double trAreaSq(double s, double a, double b, double c) {
		return s * (s - a) * (s - b) * (s - c);
	}
	
	/**
     * Retire de la liste points le point p
     * @param points La liste de points
     * @param p Le point à retirer
     */
	private void popPoint(ArrayList<WrapperPoint> points, WrapperPoint p) {
		points.remove(p);
	}

    /**
     * Retire de la liste de points un point à l'index rnd
     * @param points La liste de points
     * @param rnd Le nombre aléatoire
     * @return Le point aléatoire
     */
	private WrapperPoint popRandom(ArrayList<WrapperPoint> points) {
		int pos = generator.nextInt(points.size());
		WrapperPoint p = points.remove(pos);		
		return p;
	}

    /**
     * Choisit un point à l'index rnd dans la liste points
     * @param points La liste de points
     * @param rnd Le nombre aléatoire
     * @return Le point aléatoire
     */
	private WrapperPoint chooseRandom(ArrayList<WrapperPoint> points) {
		int pos = generator.nextInt();
		return points.get(pos);
	}

    /**
     * Retire de la liste de points le plus loin du point c
     * @param points La liste de points
     * @param c Le point c
     * @return Le point le plus loin
     */
	private WrapperPoint popFurthest(ArrayList<WrapperPoint> points, WrapperPoint c) {
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

    /**
     * Vérifie l'appartenance d'une liste de points au cercle c
     * @param points La liste de points
     * @param c Le cercle c
     * @return Vrai si tous les points sont contenus dans c.
     */
	private boolean containsAll(ArrayList<WrapperPoint> points, WrapperCircle c) {
		for (WrapperPoint p : points) {
			if (p.distance(c.getCenter()) <= c.getRadius())
				return false;
		}
		return true;
	}
}
