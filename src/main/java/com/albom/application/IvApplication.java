package com.albom.application;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import com.albom.iion.isr.data.Point;
import com.albom.iion.isr.data.ProjectDB;
import com.albom.iion.isr.data.ProjectFS;
import com.albom.iion.isr.data.ProjectFactory;
import com.albom.iion.isr.data.kharkiv.IvProjectFS;
import com.albom.iion.isr.processing.CoherentNoiseFinder;
import com.albom.iion.isr.processing.HeightIntegratorNum;
import com.albom.iion.isr.processing.TimeIntegratorSlide;

public class IvApplication {

	private ProjectDB project;
	private final String step1 = "step1";
	private final String step2 = "step2";
	private final String step3 = "step3";
	private final int[] LAGS = { 0, 7, 8, 9, 10, 11, 12 };

	private void run() {

		project = new ProjectFactory().getProject("e:/IV_2017_12_26.db3");

		if (project == null) {
			System.out.println("Error opening project.");
			System.exit(-1);
		}

		load(Paths.get("h:/Data/26-12-2017/1/"));

		temporal();

		altitudinal();

	}

	private void load(Path directory) {
		project.createTable(step1);
		ProjectFS projectFS = new IvProjectFS(project, step1);
		projectFS.load(directory);
	}

	private void temporal() {
		project.createTable(step2);
		CoherentNoiseFinder finder = new CoherentNoiseFinder(15, 4.5);
		TimeIntegratorSlide integrator = new TimeIntegratorSlide(20 * 60, 1 * 60);

		for (int lag : LAGS) {

			System.out.println("lag=" + lag);

			for (int h = 100; h < 300; h++) {

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
		HeightIntegratorNum heigthIntegrator = new HeightIntegratorNum(5);
		for (int lag : LAGS) {

			System.out.println("lag=" + lag);

			List<LocalDateTime> dates = project.getDates(step2, lag);
			for (LocalDateTime d : dates) {
				List<Point> p = project.getHeightDependency(step2, d, lag);
				List<Point> points = heigthIntegrator.integrate(p);
				project.insert(step3, points);
			}

		}

	}

	public static void main(String[] args) {

		IvApplication app = new IvApplication();
		app.run();

	}

}
