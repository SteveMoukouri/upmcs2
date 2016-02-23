package algo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class AlgoTests {
	public static void main (String [] args) {
		if (args.length < 2) {
			System.out.println("Welzl <sample_dir> <output_file>");
			System.exit(0);
		} else {
			MinCircleSolver s = new WelzlMinCircle();
			writeCircles(args, s);

			// writeTimes(args);
		}
	}
	
	private static void writeTimes(String [] args) {
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(args[1]), "utf-8"))) {
			File [] files = new File(args[0]).listFiles();
			ArrayList<File> samples = new ArrayList<>(Arrays.asList(files));
			samples.sort(new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			MinCircleSolver naive = new NaiveMinCircle();
			MinCircleSolver welzl = new WelzlMinCircle();
			for (int i = 0; i < samples.size(); i++) {
				long t, nperf, wperf;
				ArrayList<Point> pl = Tools.readPoints(samples.get(i));
				t = System.currentTimeMillis();
				naive.getMinCircle(pl);
				nperf = System.currentTimeMillis() - t;
				t = System.currentTimeMillis();
				welzl.getMinCircle(pl);
				wperf = System.currentTimeMillis() - t;
				writer.write(samples.get(i).getName().replace(".points", "") 
						+ " " + nperf + " " + wperf + "\n");
				System.out.print((i+1) + "/" + samples.size());
				System.out.flush();
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private static void writeCircles(String [] args, MinCircleSolver solver) {
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(args[1]), "utf-8"))) {
			File [] files = new File(args[0]).listFiles();
			ArrayList<File> samples = new ArrayList<>(Arrays.asList(files));
			samples.sort(new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			long t = System.currentTimeMillis();
			for (int i = 0; i < samples.size(); i++) {
				ArrayList<Point> pl = Tools.readPoints(samples.get(i));
				writer.write(samples.get(i).getName().replace(".points", "") 
						+ " " + solver.getMinCircle(pl) + "\n");
				System.out.print((i+1) + "/" + samples.size() + "\r");
			}
			long perf = System.currentTimeMillis() - t;
			System.out.println("Time: " + humanMillis(perf));
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private static String humanMillis(long millis) {
		long mins = millis / 60000;
		long secs = (millis) / 1000 - 60 * mins;
		long mill = millis % 1000;
		return mins + "m" + secs + "s" + mill;
	}
}
