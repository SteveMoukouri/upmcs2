package algorithms;

import java.awt.Point;
import java.util.ArrayList;

import supportGUI.Circle;
import supportGUI.Line;

public class DefaultTeam {

	// calculDiametre: ArrayList<Point> --> Line
	// renvoie une pair de points de la liste, de distance maximum.
	public Line calculDiametre(ArrayList<Point> points) {
		if (points.size() < 3) {
			return null;
		}

		Point p = points.get(0);
		Point q = points.get(1);

		/*******************
		 * PARTIE A ECRIRE *
		 *******************/
		return new Line(p, q);
	}

	// calculDiametreOptimise: ArrayList<Point> --> Line
	// renvoie une pair de points de la liste, de distance maximum.
	public Line calculDiametreOptimise(ArrayList<Point> points) {
		if (points.size() < 3) {
			return null;
		}

		Point p = points.get(1);
		Point q = points.get(2);

		/*******************
		 * PARTIE A ECRIRE *
		 *******************/
		return new Line(p, q);
	}

	// calculCercleMin: ArrayList<Point> --> Circle
	// renvoie un cercle couvrant tout point de la liste, de rayon minimum.
	public Circle calculCercleMin(ArrayList<Point> points) {
		if (points.isEmpty()) {
			return null;
		}

		Point center = points.get(0);
		int radius = 100;

		/*******************
		 * PARTIE A ECRIRE *
		 *******************/
		return new Circle(center, radius);
	}

	public enum EnvelConvexType {
		NAIF, JARVIS, GRAHAM
	}

	public enum EnvelConvexPrecalc {
		PIXEL, AKL, ALL, NONE
	}

	// enveloppeConvexe: ArrayList<Point> --> ArrayList<Point>
	// renvoie l'enveloppe convexe de la liste.
	public ArrayList<Point> enveloppeConvexe(ArrayList<Point> points) {
		if (points.size() < 3) {
			return null;
		}

		ArrayList<Point> enveloppe = new ArrayList<Point>();

		// Types de calcul
		EnvelConvexType algo = EnvelConvexType.GRAHAM;
		EnvelConvexPrecalc precalc = EnvelConvexPrecalc.ALL;

		if (precalc == EnvelConvexPrecalc.PIXEL
				|| precalc == EnvelConvexPrecalc.ALL)
			points.retainAll(PreCalcul_pixel(points));

		if (precalc == EnvelConvexPrecalc.AKL
				|| precalc == EnvelConvexPrecalc.ALL)
			points.retainAll(aklToussaint(points));

		// ALGO NAIF.
		if (algo == EnvelConvexType.NAIF) {
			enveloppe = enveloppeConvexeNAIF(points);
		}

		if (algo == EnvelConvexType.JARVIS) {
			enveloppe = enveloppeConvexeJARVIS(points);
		}

		if (algo == EnvelConvexType.GRAHAM) {
			enveloppe = enveloppeConvexeGRAHAM(points);
		}
		return enveloppe;
	}

	// Implémentation de l'algorithme naif de calcul de l'enveloppe convexe.
	// Complexité : teta(n^3)
	private ArrayList<Point> enveloppeConvexeNAIF(ArrayList<Point> points) {
		ArrayList<Point> enveloppe = new ArrayList<Point>();

		a_loop: for (Point A : points) {
			b_loop: for (Point B : points) {
				if (B.equals(A))
					continue;
				Point AB = vect(A, B);
				double prevProd = 0;
				boolean first = true;
				for (Point S : points) {
					if (S.equals(A) || S.equals(B))
						continue;
					Point AS = vect(A, S);
					double produitVect = prodVect(AB, AS);
					if (first) {
						prevProd = produitVect;
						first = false;
						continue;
					}
					if (produitVect * prevProd < 0) {
						continue b_loop;
					}
					if (produitVect * prevProd == 0) {
						if (AS.x * AS.x + AS.y * AS.y > AB.x * AB.x + AB.y
								* AB.y)
							continue b_loop;
						Point BS = vect(B, S);
						if (BS.x * BS.x + BS.y * BS.y > AB.x * AB.x + AB.y
								* AB.y)
							continue a_loop;
					}
				}
				enveloppe.add(B);
				enveloppe.add(A);
			}
		}
		return enveloppe;
	}

	private ArrayList<Point> enveloppeConvexeGRAHAM(ArrayList<Point> points) {
		ArrayList<Point> temp = new ArrayList<>(), temp2 = new ArrayList<>();

		int max_x = 0;
		for (Point A : points) {
			max_x = Math.max(max_x, A.x);
		}

		Point[] up = new Point[max_x + 1];
		Point[] down = new Point[max_x + 1];

		for (Point A : points) {
			Point x_up = up[A.x];
			Point x_down = down[A.x];
			if (x_up == null) {
				up[A.x] = A;
				down[A.x] = A;
			} else {
				if (x_up.y < A.y)
					up[A.x] = A;
				if (x_down.y > A.y)
					down[A.x] = A;
			}
		}

		
		for (int i = 0; i < down.length; i++)
			if (down[i] != null)
				temp.add(down[i]);
		for (int i = up.length - 1; i >= 0; i--)
			if (up[i] != null)
				temp.add(up[i]);

		System.out.println("Start");
		int i = 0;
		do {
			Point A = temp.get(i % temp.size());
			Point B = temp.get((i + 1) % temp.size());
			Point C = temp.get((i + 2) % temp.size());

			temp2.add(A);
			temp2.add(B);
			temp2.add(C);
			
			System.out.println(A + " -- " + B + " -- " + C);
			
			Point AB = vect(A, B);
			Point AC = vect(A, C);

			if (prodVect(AB, AC) < 0) {
				temp2.remove(B);
				temp.remove(B);
				i = Math.max(i - 2, -1);
			}
			if (i == 2)
				break;
		} while (++i < temp.size());

		return temp2;
	}

	private ArrayList<Point> enveloppeConvexeJARVIS(ArrayList<Point> points) {
		ArrayList<Point> enveloppe = new ArrayList<Point>();

		// Trouver P d'abcisse minimum
		Point P = points.get(0);
		for (Point tmp : points)
			if (tmp.x < P.x)
				P = tmp;

		// Trouver Q, tq PQ coté de l'enveloppe

		Point Q = null;
		outer: for (Point Q1 : points) {
			if (Q1.equals(P))
				continue;
			Point PQ1 = vect(P, Q1);
			double prevProd = 0;
			boolean first = true;
			for (Point Q2 : points) {
				if (Q2.equals(Q1) || Q2.equals(P))
					continue;
				Point PQ2 = vect(P, Q2);
				double produitVect = prodVect(PQ1, PQ2);
				if (first) {
					prevProd = produitVect;
					first = false;
					continue;
				}
				if (produitVect * prevProd < 0) {
					continue outer;
				}
			}
			Q = Q1;
			break;
		}

		Point P_init = P;
		while (Q != P_init) {
			Point PQ = vect(P, Q);
			Point R = null;
			double max = -2;
			for (Point R1 : points) {
				if (R1.equals(P) || R1.equals(Q))
					continue;
				Point QR1 = vect(Q, R1);
				double angle = (PQ.x * QR1.x + PQ.y * QR1.y)
						/ (P.distance(Q) * Q.distance(R1));
				if (max < angle) {
					R = R1;
					max = angle;
				}
			}
			enveloppe.add(P);
			enveloppe.add(Q);
			P = Q;
			Q = R;
		}
		return enveloppe;
	}

	// Effectue le précalcul (exercice 2)
	// complexité : teta(n)
	private ArrayList<Point> PreCalcul_pixel(ArrayList<Point> points) {

		ArrayList<Point> newSet = new ArrayList<Point>();

		int max_x = 0;
		for (Point A : points) {
			max_x = Math.max(max_x, A.x);
		}

		Point[] up = new Point[max_x + 1];
		Point[] down = new Point[max_x + 1];

		for (Point A : points) {
			Point x_up = up[A.x];
			Point x_down = down[A.x];
			if (x_up == null) {
				up[A.x] = A;
				down[A.x] = A;
			} else {
				if (x_up.y < A.y)
					up[A.x] = A;
				if (x_down.y > A.y)
					down[A.x] = A;
			}
		}

		for (Point p : up)
			if (p != null)
				newSet.add(p);
		for (Point p : down)
			if (p != null)
				newSet.add(p);

		return newSet;
	}

	public enum AklType {
		VECTOR, BARYCENTRE
	}

	// Effectue precalcul de AKL-Toussaint
	private ArrayList<Point> aklToussaint(ArrayList<Point> points) {
		ArrayList<Point> resultats = new ArrayList<>();

		Point A = points.get(0), B = points.get(0), C = points.get(0), D = points
				.get(0);

		for (Point p : points) {
			if (p.x < A.x)
				A = p;
			if (p.x > C.x)
				C = p;
			if (p.y < B.y)
				B = p;
			if (p.y > D.y)
				D = p;
		}

		resultats.add(A);
		resultats.add(B);
		resultats.add(C);
		resultats.add(D);
		AklType mode = AklType.BARYCENTRE;

		if (mode == AklType.VECTOR) {
			// Version utilisant le produit vectoriel

			// Vecteurs de référence pour le triangle ABC
			double ab_ac = prodVect(vect(A, B), vect(A, C)), bc_ba = prodVect(
					vect(B, C), vect(B, A)), ac_ab = prodVect(vect(A, C),
					vect(A, B));
			// Vecteurs de référence pour le triangle ADC
			double ad_ac = prodVect(vect(A, D), vect(A, C)), dc_da = prodVect(
					vect(D, C), vect(D, A)), ac_ad = prodVect(vect(A, C),
					vect(A, D));

			// Pour chaque point, on teste si il est dans ABC, ADC, ou aucun
			for (Point X : points) {
				double ab_ax = prodVect(vect(A, B), vect(A, X)), bc_bx = prodVect(
						vect(B, C), vect(B, C)), ac_ax = prodVect(vect(A, C),
						vect(A, X));
				if ((ab_ax * ab_ac >= 0) && (bc_bx * bc_ba >= 0)
						&& (ac_ax * ac_ab >= 0))
					continue;

				double ad_ax = prodVect(vect(A, D), vect(A, X)), dc_dx = prodVect(
						vect(D, C), vect(D, C));
				if ((ad_ax * ad_ac >= 0) && (dc_dx * dc_da >= 0)
						&& (ac_ax * ac_ad >= 0))
					continue;

				resultats.add(X); // Si X n'est ni dans ABC ni ADC, on l'ajoute
									// au resultat
			}
			return resultats;
		}

		// Version utilisant les coordonnées barycentriques
		for (Point X : points) {
			if (X.equals(A) || X.equals(B) || X.equals(C) || X.equals(D))
				continue;
			// Pour le triangle ABC.
			try {
				double l1 = ((double) (B.y - C.y) * (X.x - C.x) + (C.x - B.x)
						* (X.y - C.y))
						/ ((B.y - C.y) * (A.x - C.x) + (C.x - B.x)
								* (A.y - C.y));
				double l2 = ((double) (C.y - A.y) * (X.x - C.x) + (A.x - C.x)
						* (X.y - C.y))
						/ ((B.y - C.y) * (A.x - C.x) + (C.x - B.x)
								* (A.y - C.y));
				double l3 = 1 - l2 - l1;
				if (l1 >= 0 && l2 >= 0 && l3 >= 0 && l1 <= 1 && l2 <= 1
						&& l3 <= 1)
					continue;

				// Pour le triangle ADC.

				l1 = ((double) (D.y - C.y) * (X.x - C.x) + (C.x - D.x)
						* (X.y - C.y))
						/ ((D.y - C.y) * (A.x - C.x) + (C.x - D.x)
								* (A.y - C.y));
				l2 = ((double) (C.y - A.y) * (X.x - C.x) + (A.x - C.x)
						* (X.y - C.y))
						/ ((D.y - C.y) * (A.x - C.x) + (C.x - D.x)
								* (A.y - C.y));
				l3 = 1 - l2 - l1;
				if (l1 >= 0 && l2 >= 0 && l3 >= 0 && l1 <= 1 && l2 <= 1
						&& l3 <= 1)
					continue;
				resultats.add(X); // Si X n'est ni dans ABC ni ADC, on l'ajoute
									// au
									// resultat
			} catch (Exception e) {
				System.out.println(e);
				throw (e);
			}
		}

		return resultats;
	}

	private void print(String s) {
		System.out.println(s);
	}

	// renvoie la coordonnée Z du produit vectoriel des vecteurs v1 et v2
	static private double prodVect(Point v1, Point v2) {
		return v1.x * v2.y - v1.y * v2.x;
	}

	// renvoie le vectuur P1P2
	static private Point vect(Point p1, Point p2) {
		return new Point(p1.x - p2.x, p1.y - p2.y);
	}
}