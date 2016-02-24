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
		System.out.println("Before: " + points.size());
		Point a = points.get(0), b = points.get(0), c = points.get(0), d = points.get(0);
		for (Point p : points) { 
			if (p.getX() < a.getX())
				a = p;
			if (p.getY() < d.getY())
				d = p;
			if (p.getX() > c.getX())
				c = p;
			if (p.getY() > b.getY())
				b = p;
		}
		for (Iterator<Point> iterator = points.iterator(); iterator.hasNext();) {
			Point p = iterator.next();
			int ax = a.getX(), ay = a.getY(), bx = b.getX(), by = b.getY();
			int cx = c.getX(), cy = c.getY(), dx = d.getX(), dy = d.getY();
			int px = p.getX(), py = p.getY();
			double l11 = (((by-cy)*(px-cx))+((cx-bx)*(py-cy))) /
					(((by-cy)*(ax-cx))+((cx-bx)*(ay-cy)));
			double l12 = (((cy-ay)*(px-cx))+((ax-cx)*(py-cy))) /
					(((by-cy)*(ax-cx))+((cx-bx)*(ay-cy)));
			double l13 = 1-l11-l12;
			double l21 = (((cy-dy)*(px-dx))+((dx-cx)*(py-dy))) / 
					(((cy-dy)*(ax-dx))+((dx-cx)*(ay-dy)));
			double l22 = (((dy-ay)*(px-dx))+((ax-dx)*(py-dy))) /
					(((cy-dy)*(ax-dx))+((dx-cx)*(ay-dy)));
			double l23 = 1-l21-l22;
			System.out.println("abc:" + l11 + "," + l12 + "," + l13 + "\nacd:" + l21 + "," + l22 + "," + l23);
			if (zeroToOne(l11, l12, l13) || zeroToOne(l21, l22, l23))
				iterator.remove();
		}
		System.out.println("After: " + points.size());
		return points;
	}
	
	public static boolean zeroToOne(double a, double b, double c) {
		return (a >= 0 && b >= 0 && c >= 0 && a <= 1 && b <= 1 && c <= 1);
	}
}
