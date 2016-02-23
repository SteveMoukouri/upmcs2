package algo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

public class Tools {
	
	public static ArrayList<Point> readPoints(File fp) {
		ArrayList<Point> result = new ArrayList<Point>();
		try (BufferedReader b = new BufferedReader(new FileReader(fp))) {
			String line = b.readLine();
			while (line != null) {
				String [] coords = line.split(" ");
				Point e = new Point(Integer.valueOf(coords[0]), Integer.valueOf(coords[1]));
				result.add(e);
				line = b.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Filtrage AKL barycentres
	 * @param points
	 * @return
	 */
	public static ArrayList<Point> aklPreCalc(ArrayList<Point> points) {
		Point a = null, b = null, c = null, d = null;
		for (Point p : points) { 
			if (a == null || p.getX() < a.getX())
				a = p;
			if (b == null || p.getY() < b.getY())
				b = p;
			if (c == null || p.getX() > c.getX())
				c = p;
			if (d == null || p.getY() > d.getY())
				d = p;
		}
		for (Iterator<Point> iterator = points.iterator(); iterator.hasNext();) {
			Point p = iterator.next();
			double l11 = ((b.getY() - c.getY()) * (p.getX() - c.getX()) + (c.getX() - b.getX()) * (p.getY() - c.getY()))/
					((b.getY() - c.getY()) * (a.getX() - c.getX()) + (c.getX() - b.getX()) * (a.getY() - c.getY()));
			double l12 = ((c.getY() - a.getY()) * (p.getX() - c.getX()) + (a.getX() - c.getX()) * (p.getY() - c.getY()))/
					((b.getY() - c.getY()) * (a.getX() - c.getX()) + (c.getX() - b.getX()) * (a.getY() - c.getY()));
			double l13 = 1 - l11 - l12;
			double l21 = ((c.getY() - d.getY()) * (p.getX() - d.getX()) + (d.getX() - c.getX()) * (p.getY() - d.getY()))/
					((c.getY() - d.getY()) * (a.getX() - d.getX()) + (d.getX() - c.getX()) * (a.getY() - d.getY()));
			double l22 = ((d.getY() - a.getY()) * (p.getX() - d.getX()) + (a.getX() - d.getX()) * (p.getY() - d.getY()))/
					((c.getY() - d.getY()) * (a.getX() - d.getX()) + (d.getX() - c.getX()) * (a.getY() - d.getY()));
			double l23 = 1 - l21 - l22;
			if (!(zeroToOne(l11, l12, l13) || zeroToOne(l21, l22, l23)))
				iterator.remove();
		}
		return points;
	}
	
	public static boolean zeroToOne(double a, double b, double c) {
		return (a >= 0 && b >= 0 && c >= 0 && a <= 1 && b <= 1 && c <= 1);
	}
}
