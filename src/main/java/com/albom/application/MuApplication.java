package com.albom.application;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import com.albom.iion.isr.data.Point;
import com.albom.iion.isr.data.ProjectDB;
import com.albom.iion.isr.data.ProjectFS;
import com.albom.iion.isr.data.ProjectFactory;
import com.albom.iion.isr.data.mu.MuData;
import com.albom.iion.isr.data.mu.MuRawData;
import com.albom.iion.isr.data.mu.MuSession;
import com.albom.iion.isr.data.mu.MuSessionFS;
import com.albom.iion.isr.processing.CoherentNoiseFinder;
import com.albom.iion.isr.processing.HeightIntegratorNum;
import com.albom.iion.isr.processing.Interpolator;
import com.albom.iion.isr.processing.TimeIntegrator;
import com.albom.iion.isr.processing.TimeIntegratorBlock;
import com.albom.iion.isr.processing.TimeIntegratorNum;
import com.albom.iion.isr.processing.TimeIntegratorPoints;
import com.albom.iion.isr.processing.TimeIntegratorSlide;
import com.albom.iion.isr.processing.mu.Acf;
import com.albom.iion.isr.processing.mu.Altitude;
import com.albom.utils.Directory;
import com.albom.utils.DoubleTableLogger;
import com.albom.utils.PointLogger;
import com.albom.utils.StringLogger;
import com.albom.utils.Time;

public class MuApplication {

	private void load(ProjectDB project) {
		String step1 = "step1";
		project.createTable(step1);
		ProjectFS projectFS = new ProjectFS(project, step1);
		projectFS.load("d:\\test");
	}

	private void temporal(ProjectDB project) {
		String step1 = "step1";
		String step2 = "step2";

		int start = 1333; // head.getJstart()
		int sampling = 32; // head.getJsint()
		int zenith = 20; // new MuDirection(head.getIbeam()[0]).getZenith()

		for (int lag = 0; lag <= 6; lag++) {

			project.createTable(step2);

			CoherentNoiseFinder finder = new CoherentNoiseFinder(20, 4);
			TimeIntegratorSlide integrator_2 = new TimeIntegratorSlide(20 * 60, 10 * 60);

			for (int h = 0; h < 256; h++) {
				List<Point> points;
				List<Boolean> labels;

				points = project.getTimeDependency(step1, h, lag);
//				PointLogger.log("d:/all/" + String.valueOf(lag) + "/"
//						+ (int) (Altitude.getAbsolute(h, start, sampling, zenith)) + ".txt", points);
				labels = finder.find(points);
//				PointLogger.log("d:/all/" + String.valueOf(lag) + "/" + "filtered_"
//						+ (int) (Altitude.getAbsolute(h, start, sampling, zenith)) + ".txt", points, labels);
				points = integrator_2.integrate(points, labels);
//				PointLogger.log("d:/all/" + String.valueOf(lag) + "/" + "integrated_"
//						+ (int) (Altitude.getAbsolute(h, start, sampling, zenith)) + ".txt", points);
//				System.out.println(h);

				project.begin();
				for (Point point : points) {
					project.insert(step2, point);
				}
				project.commit();

			}
		}
	}

	private void altitudinal(ProjectDB project) {
		String step2 = "step2";
		String step3 = "step3";

		project.createTable(step3);

		for (int lag = 0; lag <= 6; lag++) {

			ArrayList<LocalDateTime> dates = project.getDates(step2, lag);

			HeightIntegratorNum heigthIntegrator = new HeightIntegratorNum(7);

			for (LocalDateTime d : dates) {
				ArrayList<Point> p = project.getHeightDependency(step2, d, lag);
				ArrayList<Point> points = heigthIntegrator.integrate(p);
				project.begin();
				for (Point point : points) {
					project.insert(step3, point);
				}
				project.commit();
			}
		}
	}

	private void snr(ProjectDB project) {

		String step3 = "step3";
		String step4 = "step4";

		project.createTable(step4);

		int lag = 0;

		ArrayList<LocalDateTime> dates = project.getDates(step3, lag);

		for (LocalDateTime d : dates) {
			ArrayList<Point> p = project.getHeightDependency(step3, d, lag);
			int length = p.size();

			ArrayList<Double> q = new ArrayList<>(length);
			double pn = 0;
			int c = 0;
			for (int h = 120; h < 256; h++) {
				pn += p.get(h).getValue();
				c++;
			}
			pn /= c;
			for (Point v : p) {
				q.add((v.getValue() - pn) / pn);
			}
			project.begin();
			for (int h = 0; h < 256; h++) {
				project.insert(step4, new Point(d, h, lag, q.get(h)));
			}
			project.commit();
		}
	}

	private void power(ProjectDB project) {

		String step3 = "step2"; // TODO change to step3
		String step5 = "step5";
		
		String step6 = "step6";

		project.createTable(step5);
		project.createTable(step6);

		int lag = 0;

		ArrayList<LocalDateTime> dates = project.getDates(step3, lag);

		for (LocalDateTime d : dates) {
			ArrayList<Point> p = project.getHeightDependency(step3, d, lag);
			int length = p.size();

			ArrayList<Double> q = new ArrayList<>(length);
			ArrayList<Double> ps = new ArrayList<>(length);
			double pn = 0;
			int c = 0;
			for (int h = 120; h < 256; h++) {
				pn += p.get(h).getValue();
				c++;
			}
			pn /= c;
			for (Point v : p) {
				ps.add(v.getValue() - pn);
				q.add((v.getValue() - pn)/pn);
			}
			project.begin();
			for (int h = 0; h < 256; h++) {
				project.insert(step5, new Point(d, h, lag, ps.get(h)));
				project.insert(step6, new Point(d, h, lag, q.get(h)));
			}
			project.commit();
		}
	}

	private void print(ProjectDB project) {

		String step4 = "step4";

		int lag = 0;

		int start = 1333; // head.getJstart()
		int sampling = 32; // head.getJsint()
		int zenith = 20; // new MuDirection(head.getIbeam()[0]).getZenith()

		ArrayList<LocalDateTime> dates = project.getDates(step4, lag);

		Path file = Paths.get("d:/all/out.txt");
		try (BufferedWriter out = Files.newBufferedWriter(file, Charset.forName("US-ASCII"))) {

			out.write(String.format(Locale.US, "%9.3f ", 0.0));
			for (int i = 0; i < dates.size(); i++) {
				out.write(String.format(Locale.US, "%9.3f ", Time.toDecimal(dates.get(i))));

			}
			out.write("\n");

			for (int h = 0 + 7 / 2; h < 256 - 7 / 2; h++) {
				ArrayList<Point> values = project.getTimeDependency(step4, h, lag);
				out.write(String.format(Locale.US, "%9.3f ", Altitude.getAbsolute(h, start, sampling, zenith)));
				for (int i = 0; i < dates.size(); i++) {
					out.write(String.format(Locale.US, "%9.3f ", values.get(i).getValue()));
				}
				out.write("\n");
			}

		} catch (IOException x) {
			System.out.println("Exception thrown: " + x);
		}
	}

	private void printPower(ProjectDB project) {

		String step4 = "step5";

		int lag = 0;

		int start = 1333; // head.getJstart()
		int sampling = 32; // head.getJsint()
		int zenith = 20; // new MuDirection(head.getIbeam()[0]).getZenith()

		ArrayList<LocalDateTime> dates = project.getDates(step4, lag);

		Path file = Paths.get("d:/IS_power.txt");
		try (BufferedWriter out = Files.newBufferedWriter(file, Charset.forName("US-ASCII"))) {

			out.write(String.format(Locale.US, "%9.3f ", 0.0));
			for (int i = 0; i < dates.size(); i++) {
				out.write(String.format(Locale.US, "%9.3f ", Time.toDecimal(dates.get(i))));

			}
			out.write("\n");

			for (int h = 0 + 7 / 2; h < 256 - 7 / 2; h++) {
				ArrayList<Point> values = project.getTimeDependency(step4, h, lag);
				out.write(String.format(Locale.US, "%9.3f ", Altitude.getAbsolute(h, start, sampling, zenith)));
				for (int i = 0; i < dates.size(); i++) {
					out.write(String.format(Locale.US, "%9.3f ", values.get(i).getValue()));
				}
				out.write("\n");
			}

		} catch (IOException x) {
			System.out.println("Exception thrown: " + x);
		}
		
		
		
		file = Paths.get("d:/SNR.txt");
		try (BufferedWriter out = Files.newBufferedWriter(file, Charset.forName("US-ASCII"))) {

			out.write(String.format(Locale.US, "%9.3f ", 0.0));
			for (int i = 0; i < dates.size(); i++) {
				out.write(String.format(Locale.US, "%9.3f ", Time.toDecimal(dates.get(i))));

			}
			out.write("\n");

			for (int h = 0 + 7 / 2; h < 256 - 7 / 2; h++) {
				ArrayList<Point> values = project.getTimeDependency("step6", h, lag);
				out.write(String.format(Locale.US, "%9.3f ", Altitude.getAbsolute(h, start, sampling, zenith)));
				for (int i = 0; i < dates.size(); i++) {
					out.write(String.format(Locale.US, "%9.3f ", values.get(i).getValue()));
				}
				out.write("\n");
			}

		} catch (IOException x) {
			System.out.println("Exception thrown: " + x);
		}
		
	}
	
	private void incline(ProjectDB project) {

		for (int lag = 0; lag <= 6; lag++) {

			ArrayList<LocalDateTime> dates = project.getDates("step2", lag);

			for (LocalDateTime d : dates) {

				SimpleRegression regression = new SimpleRegression();
				ArrayList<Point> points = project.getHeightDependency("step2", d, lag);
				for (Point p : points) {
					double altitude = Altitude.getAbsolute(p.getAlt(), 1333, 32, 20);
					if (altitude > 700) {
						regression.addData(altitude, p.getValue());
					}
				}

				double[][] data = new double[points.size()][6];
				for (int i = 0; i < points.size(); i++) {
					Point p = points.get(i);
					double altitude = Altitude.getAbsolute(p.getAlt(), 1333, 32, 20);
					data[i][0] = altitude;
					data[i][1] = p.getValue();
					data[i][2] = regression.getSlope() * altitude + regression.getIntercept();
					data[i][3] = regression.getSlope();
					data[i][4] = regression.getIntercept();
					data[i][5] = data[i][1]-data[i][2]; 
				}
				String fileName =   d.toString().replaceAll(":", "_") + ".txt";

				// System.out.println(fileName);

				DoubleTableLogger.log("d:/all/" + String.valueOf(lag) + "/" + fileName, data);

				StringLogger.log("d:/all/" + String.valueOf(lag) + "/plot.gp",
						"set output \"" + fileName + ".png\"" + "\n" + "set title \"" + d.toString() + "\"" + "\n"
								+ "plot \"" + fileName + "\" using ($2):($1) w l notitle , \"" + fileName
								+ "\" using ($3):($1) w l notitle \n");
			}
		}
	}

	private void power2(ProjectDB project) {

		final String step3 = "step3";
		final String step5 = "step5";

		project.createTable(step5);

		int lag = 0;
				
		ArrayList<LocalDateTime> dates = project.getDates(step3, lag);

		for (LocalDateTime d : dates) {

			SimpleRegression regression = new SimpleRegression();
			ArrayList<Point> points = project.getHeightDependency(step3, d, lag);
			for (Point p : points) {
				double altitude = Altitude.getAbsolute(p.getAlt(), 1333, 32, 20);
				if (altitude > 700) {
					regression.addData(altitude, p.getValue());
				}
			}

			project.begin();
			for (int h = 0; h < points.size(); h++) {
				Point p = points.get(h);
				double altitude = Altitude.getAbsolute(p.getAlt(), 1333, 32, 20);
				project.insert(step5, new Point(d, h, lag, p.getValue()-(regression.getSlope()*altitude
						 + regression.getIntercept())));
			}
			project.commit();
		
		}

	}

	private void run(String[] args) {

		ProjectDB project = new ProjectFactory().getProject("d:/data.db3");

		if (project == null) {
			System.exit(-1);
		}

		 load(project);
		temporal(project);
		// altitudinal(project);
		// snr(project);
		power(project);
		printPower(project);

//		 print(project);

//		 incline(project);

		project.close();

	}

	public static void main(String[] args) {
		MuApplication app = new MuApplication();
		app.run(args);
	}

}
