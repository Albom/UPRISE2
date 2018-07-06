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

public class MuApplication {

	private int start;
	private int sampling;
	private int zenith;
	private int nH;
	private int nLag;

	private final String step1 = "step1";
	private final String step2 = "step2";
	private final String step3 = "step3";

	private ProjectDB project;

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
				if (altitude < 300) {
					List<Point> points = project.getAcf(step3, date, h);
					double[] acf = acfToArray(points);

					int tiEstimated = 0, teEstimated = 0;
					double kEstimated = 0;

					double deltaMin = Double.MAX_VALUE;
					for (double k = 0.95; k <= 1.5; k += 0.01) {
						acf[0] = k * acf[1];
						for (int ti = tMin; ti <= tMax; ti += 10) {
							for (int te = ti; te <= tMax; te += 10) {
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
			java.nio.file.Files.write(Paths.get("e:/MU_2017_12/parameters.txt"), buffer.toString().getBytes("utf-8"),
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void snr() {
		int lag = 0;
		List<LocalDateTime> dates = project.getDates(step3, lag);

		StringBuffer buffer = new StringBuffer();
		for (LocalDateTime date : dates) {
			List<Point> points = project.getHeightDependency(step3, date, lag);

			double pN = Double.MAX_VALUE;
			for (Point p : points) {
				if (p.getValue() < pN) {
					pN = p.getValue();
				}
			}

			int i = 0;
			for (Point p : points) {
				String line = "\"" + date.toString() + "\"" + ";"
						+ String.valueOf(
								Altitude.getAbsolute(i++, start, sampling, zenith) + ";" + (p.getValue() - pN) / pN)
						+ "\n";

				buffer.append(line);
			}
			buffer.append("\n");

		}
		try {
			java.nio.file.Files.write(Paths.get("e:/MU_2017_12/snr.txt"), buffer.toString().getBytes("utf-8"),
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void run(String[] args) {

		project = new ProjectFactory().getProject("e:/MU_2017_12.db3");

		if (project == null) {
			System.out.println("Error opening project.");
			System.exit(-1);
		}

		load(Paths.get("h:/Data/MU/MI2972"));
		load(Paths.get("h:/Data/MU/MI2973"));
		load(Paths.get("h:/Data/MU/MI2974"));

		getProperties();

		temporal();
		altitudinal();
		inverse();
		snr();

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
