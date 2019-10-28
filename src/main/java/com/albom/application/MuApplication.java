package com.albom.application;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import com.albom.iion.isr.data.Point;
import com.albom.iion.isr.data.ProjectDB;
import com.albom.iion.isr.data.ProjectFS;
import com.albom.iion.isr.data.ProjectFactory;
import com.albom.iion.isr.data.mu.MuProjectFS;
import com.albom.iion.isr.processing.CoherentNoiseFinder;
import com.albom.iion.isr.processing.HeightIntegratorNum;
import com.albom.iion.isr.processing.TimeIntegratorSlide;
import com.albom.iion.isr.processing.mu.AcfLibrary;
import com.albom.iion.isr.processing.mu.Altitude;
import com.albom.util.Time;

public class MuApplication {

	private int start;
	private int sampling;
	private int zenith;
	private int nH;
	private int nLag;

	private final String step1 = "step1";
	private final String step2 = "step2";
	private final String step3 = "step3";
	private final String step4 = "step4";

	private ProjectDB project;

	// private final String projectDirectory = "e:/MU_2017_12/";
	// private final String[] fileNames = { "h:/Data/MU/MU20171225" };

	private final String projectDirectory = "e:/MU_2017_09/";
	private final String[] fileNames = { "h:/Data/MU/MI2972", "h:/Data/MU/MI2973", "h:/Data/MU/MI2974" };

	private void load(Path directory) {
		project.createTable(step1);
		ProjectFS projectFS = new MuProjectFS(project, step1);
		projectFS.load(directory);
	}

	private void temporal() {

		project.createTable(step2);

		CoherentNoiseFinder finder = new CoherentNoiseFinder(20, 4.0);

		TimeIntegratorSlide integrator = new TimeIntegratorSlide(1 * 60 * 60, 15 * 60);

		for (int lag = 0; lag < nLag; lag++) {

			System.out.println("lag=" + lag);

			for (int h = 0; h < nH; h++) {

				System.out.println("h=" + h);

				List<Point> points = project.getTimeDependency(step1, h, lag);

				List<Boolean> labels = finder.find(points);
				points = integrator.integrate(points, labels);
				project.insert(step2, points);
			}
		}
	}

	private void altitudinal() {

		project.createTable(step3);
		HeightIntegratorNum heigthIntegrator = new HeightIntegratorNum(11);

		for (int lag = 0; lag < nLag; lag++) {

			System.out.println("lag=" + lag);

			List<LocalDateTime> dates = project.getDates(step2, lag);

			for (LocalDateTime d : dates) {
				List<Point> p = project.getHeightDependency(step2, d, lag);
				List<Point> points = heigthIntegrator.integrate(p);
				project.insert(step3, points);
			}
		}
	}

	private void subNoise() {
		project.createTable(step4);
		for (int lag = 0; lag < nLag; lag++) {
			List<LocalDateTime> dates = project.getDates(step3, lag);
			for (LocalDateTime d : dates) {
				List<Point> points = project.getHeightDependency(step3, d, lag);

				double noise = Double.MAX_VALUE;
				for (Point p : points) {
					double val = p.getValue();
					if (val < noise) {
						noise = val;
					}
				}

				for (Point p : points) {
					p.setValue(p.getValue() - noise);
				}

				project.insert(step4, points);
			}
		}
	}

	private double[] acfToArray(List<Point> points) {
		double[] acf = new double[points.size() + 1];
		for (Point p : points) {
			acf[p.getLag()] = p.getValue();
		}
		return acf;
	}

	private void inverse() {

		final int tMax = 3000;
		final int tMin = 600;
		AcfLibrary library = new AcfLibrary(tMin, tMax, 10);
		double[] acfTheor = new double[7];

		StringBuffer buffer = new StringBuffer();
		List<LocalDateTime> dates = project.getDates(step3, 1);
		for (LocalDateTime date : dates) {
			System.out.println(date);
			for (int h = 0; h < nH; h++) {
				double altitude = Altitude.getAbsolute(h, 1333, sampling, zenith);
				// if ((altitude > 268) && (altitude < 272)) {
				if (altitude < 400) {
					List<Point> points = project.getAcf(step3, date, h);
					double[] acf = acfToArray(points);

					int tiEstimated = 0, teEstimated = 0;
					double kEstimated = 0;

					double deltaMin = Double.MAX_VALUE;
					for (double k = 0.95; k <= 1.5; k += 0.05) {
						acf[0] = k * acf[1];
						for (int ti = tMin; ti <= tMax; ti += 20) {
							for (int te = ti; te <= tMax; te += 20) {
								library.getAcf(ti, te, acfTheor);
								double d = 0;
								for (int lag = 1; lag <= 6; lag++) {
									d += Math.pow(acf[lag] - acfTheor[lag] * acf[0], 2);
								}
								if (d < deltaMin) {
									deltaMin = d;
									tiEstimated = ti;
									teEstimated = te;
									kEstimated = k;
								}
							}
						}
					}
					buffer.append(String.format(Locale.US, "\"%10s\";%8.1f;%5d;%5d;%7.2f;%7.3E;", date.toString(),
							altitude, tiEstimated, teEstimated, kEstimated, deltaMin));
					acf[0] = acf[1] * kEstimated;
					for (int lag = 0; lag <= 6; lag++) {
						buffer.append(String.format(Locale.US, "%8.4f;", acf[lag]));
					}

					library.getAcf(tiEstimated, teEstimated, acfTheor);
					for (int lag = 0; lag <= 6; lag++) {
						buffer.append(String.format(Locale.US, "%8.4f;", acfTheor[lag] * acf[0]));
					}

					buffer.append(String.valueOf(h) + "\n");
				}
			}
			buffer.append("\n");
		}

		try {
			java.nio.file.Files.write(Paths.get(projectDirectory + "parameters.txt"),
					buffer.toString().getBytes("utf-8"), StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void snr() {
		out(true);
	}

	private void qh2() {
		out(false);
	}

	private void out(boolean snr) {
		final int lag = 0;
		List<LocalDateTime> dates = project.getDates(step4, lag);
		final int nT = dates.size();

		double[][] table = new double[nT][nH];

		int i = 0;
		for (LocalDateTime date : dates) {
			List<Point> pointsSignal = project.getHeightDependency(step4, date, lag);
			List<Point> pointsSignalAndNoise = project.getHeightDependency(step3, date, lag);
			for (int h = 0; h < nT; h++) {
				int alt = pointsSignal.get(h).getAlt();
				double ps = pointsSignal.get(h).getValue();
				double psn = pointsSignalAndNoise.get(h).getValue();
				table[i][alt] = ps / (psn - ps);
			}
			i++;
		}

		StringBuffer buffer = new StringBuffer();

		for (int t = 0; t < nT; t++) {
			for (int h = 0; h < nH; h++) {
				double alt = Altitude.getAbsolute(h, start, sampling, zenith);
				buffer.append(dates.get(t).toString() + " " + String.format(Locale.US, "%10.4f", alt) + " "
						+ String.format(Locale.US, "%10.4f", snr ? table[t][h] : table[t][h] * alt * alt));
				buffer.append("\n");
			}
			buffer.append("\n");
		}

		try {
			java.nio.file.Files.write(Paths.get(projectDirectory + (snr ? "snr.txt" : "qh2.txt")),
					buffer.toString().getBytes("utf-8"), StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void print_snr() {
		final int lag = 0;
		List<LocalDateTime> dates = project.getDates(step4, lag);
		final int nT = dates.size();

		double[][] table = new double[nT][nH];

		int i = 0;
		for (LocalDateTime date : dates) {
			List<Point> pointsSignal = project.getHeightDependency(step4, date, lag);
			List<Point> pointsSignalAndNoise = project.getHeightDependency(step3, date, lag);
			for (int h = 0; h < nT; h++) {
				int alt = pointsSignal.get(h).getAlt();
				double ps = pointsSignal.get(h).getValue();
				double psn = pointsSignalAndNoise.get(h).getValue();
				table[i][alt] = ps / (psn - ps);
			}
			i++;
		}

		StringBuffer buffer = new StringBuffer();

		buffer.append(String.format(Locale.US, "%7d", 0));
		for (int h = 0; h < nH; h++) {
			double alt = Altitude.getAbsolute(h, start, sampling, zenith);
			buffer.append(String.format(Locale.US, "%7.1f", alt));
		}
		buffer.append("\n");

		for (int t = 0; t < nT; t++) {
			buffer.append(String.format(Locale.US, "%7.4f", Time.toDecimal(dates.get(t))));
			for (int h = 0; h < nH; h++) {
				buffer.append(String.format(Locale.US, "%7.4f", table[t][h]));
			}
			buffer.append("\n");
		}

		try {
			java.nio.file.Files.write(Paths.get(projectDirectory + ("snr_table.txt")),
					buffer.toString().getBytes("utf-8"), StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void print_acf() {
		List<LocalDateTime> datesLag0 = project.getDates(step4, 0);
		List<LocalDateTime> datesLag1_6 = project.getDates(step4, 1);

		StringBuffer buffer = new StringBuffer();
		for (LocalDateTime date1_6 : datesLag1_6) {
			LocalDateTime date0 = Time.nearest(date1_6, datesLag0);
			for (int h = 0; h < nH; h++) {
				double alt = Altitude.getAbsolute(h, start, sampling, zenith);
				if (alt > 200 && alt < 400) {
					buffer.append(date0.toString() + " ");
					buffer.append(date1_6.toString() + " ");
					buffer.append(String.format(Locale.US, "%7.1f", alt) + " ");
					List<Point> points0 = project.getAcf(step4, date0, h);
					for (Point p : points0){
						buffer.append(String.format(Locale.US, "%7.4f", p.getValue()) + " ");
					}
					List<Point> points1_6 = project.getAcf(step4, date1_6, h);
					for (Point p : points1_6){
						buffer.append(String.format(Locale.US, "%7.4f", p.getValue()) + " ");
					}
					buffer.append("\n");
				}
			}
		}
		try {
			java.nio.file.Files.write(Paths.get(projectDirectory + "acf.txt"), buffer.toString().getBytes("utf-8"),
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void hm() {
		int lag = 0;
		List<LocalDateTime> dates = project.getDates(step4, lag);

		StringBuffer buffer = new StringBuffer();
		for (LocalDateTime date : dates) {
			List<Point> points = project.getHeightDependency(step4, date, lag);

			double m = Double.MIN_VALUE;
			int h = 0;
			int i = 0;
			for (Point p : points) {
				double alt = Altitude.getAbsolute(p.getAlt(), start, sampling, zenith);
				double qh2 = p.getValue() * alt * alt;
				if (alt > 200 && alt < 400 && qh2 > m) {
					m = qh2;
					h = i;
				}
				i++;
			}

			String line = date.toString() + " " + String.valueOf(Altitude.getAbsolute(h, start, sampling, zenith)) + " "
					+ String.valueOf(m);

			buffer.append(line);

			buffer.append("\n");

		}
		try {
			java.nio.file.Files.write(Paths.get(projectDirectory + "hm.txt"), buffer.toString().getBytes("utf-8"),
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void run(String[] args) {

		project = new ProjectFactory().getProject(projectDirectory + "MU_2017_09.db3");

		if (project == null) {
			System.out.println("Error opening project.");
			System.exit(-1);
		}

		// for (String fileName : fileNames) {
		// load(Paths.get(fileName));
		// }

		getProperties();

		// temporal();
		// altitudinal();
		// subNoise();
		// snr();
		// qh2();
		// hm();
		// print_snr();
		print_acf();
		// inverse();

		project.close();

	}

	private void getProperties() {
		start = Integer.valueOf(project.getProperty("start"));
		sampling = Integer.valueOf(project.getProperty("sampling"));
		zenith = Integer.valueOf(project.getProperty("zenith"));
		nH = Integer.valueOf(project.getProperty("nh"));
		nLag = Integer.valueOf(project.getProperty("nlag"));
	}

	public static void main(String[] args) {
		MuApplication app = new MuApplication();
		app.run(args);
	}

}
