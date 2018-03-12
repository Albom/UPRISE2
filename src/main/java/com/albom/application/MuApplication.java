package com.albom.application;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import com.albom.iion.isr.data.Point;
import com.albom.iion.isr.data.ProjectDB;
import com.albom.iion.isr.data.ProjectFS;
import com.albom.iion.isr.data.ProjectFactory;
import com.albom.iion.isr.data.mu.MuProjectFS;
import com.albom.iion.isr.processing.CoherentNoiseFinder;
import com.albom.iion.isr.processing.ForwardProblem;
import com.albom.iion.isr.processing.HeightIntegratorNum;
import com.albom.iion.isr.processing.TimeIntegratorSlide;
import com.albom.iion.isr.processing.mu.Altitude;
import com.albom.iion.isr.radars.MURadar;
import com.albom.physics.UnitPrefix;
import com.albom.utils.Time;

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
		TimeIntegratorSlide integrator = new TimeIntegratorSlide(60 * 60, 30 * 60);

		for (int lag = 0; lag < nLag; lag++) {

			System.out.println("lag=" + lag);

			for (int h = 0; h < nH; h++) {

				System.out.println("h=" + h);

				List<Point> points = project.getTimeDependency(step1, h, lag);
				List<Boolean> labels = finder.find(points);
				points = integrator.integrate(points, labels);

				project.begin();
				for (Point point : points) {
					project.insert(step2, point);
				}
				project.commit();

			}
		}
	}

	private void altitudinal() {

		project.createTable(step3);

		for (int lag = 0; lag < nLag; lag++) {

			System.out.println("lag=" + lag);

			List<LocalDateTime> dates = project.getDates(step2, lag);

			HeightIntegratorNum heigthIntegrator = new HeightIntegratorNum(7);

			for (LocalDateTime d : dates) {
				List<Point> p = project.getHeightDependency(step2, d, lag);
				List<Point> points = heigthIntegrator.integrate(p);
				project.begin();
				for (Point point : points) {
					project.insert(step3, point);
				}
				project.commit();
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

		ForwardProblem forward = new ForwardProblem(MURadar.WAVE_LENGTH);
		double[] acfTheor = new double[7];
		final double DELTA_TAU = UnitPrefix.micro(MURadar.DISTANCE_BETWEEN_PULSES);

		// System.out.println(Arrays.toString(acfTheor));

		List<LocalDateTime> dates = project.getDates(step3, 1);
		for (LocalDateTime date : dates) {
			for (int h = 0; h < nH; h++) {
				double altitude = Altitude.getAbsolute(h, start, sampling, zenith);
				if ((altitude > 200) && (altitude < 300)) {
					List<Point> points = project.getAcf(step3, date, h);
					double[] acf = acfToArray(points);

					double tiEstimated = 0, teEstimated = 0, kEstimated = 0;

					double deltaMin = Double.MAX_VALUE;
					for (double k = 0.8; k < 1.2; k += 0.02) {
						acf[0] = k * acf[1];
						for (double ti = 500; ti < 2000; ti += 50) {
							for (double te = ti; te < 2000; te += 50) {
								forward.acf(ti, te, DELTA_TAU, acfTheor);
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
					// return;
					System.out.print(String.format(Locale.US, "%8.5f\t%8.1f\t%5.0f\t%5.0f\t%7.2f\t%7.3E\t\t",
							Time.toDecimal(date), altitude, tiEstimated, teEstimated, kEstimated, deltaMin));
					acf[0] = acf[1] * kEstimated;
					for (int lag = 0; lag <= 6; lag++) {
						System.out.print(String.format(Locale.US, "%6.3f\t", acf[lag] / acf[0]));
					}
					System.out.print("\t");

					forward.acf(tiEstimated, teEstimated, DELTA_TAU, acfTheor);
					for (int lag = 0; lag <= 6; lag++) {
						System.out.print(String.format(Locale.US, "%6.3f\t", acfTheor[lag]));
					}

					System.out.println();

					// break;
				}
			}
			System.out.println();
			// break;
		}
	}

	private void run(String[] args) {

		project = new ProjectFactory().getProject("d:/data.db3");

		if (project == null) {
			System.exit(-1);
		}

		load(Paths.get("d:/test"));

		getProperties();

		temporal();
		altitudinal();
		inverse();

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
