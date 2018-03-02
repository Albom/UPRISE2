package com.albom.application;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import com.albom.iion.isr.data.Point;
import com.albom.iion.isr.data.ProjectDB;
import com.albom.iion.isr.data.ProjectFS;
import com.albom.iion.isr.data.ProjectFactory;
import com.albom.iion.isr.data.mu.MuProjectFS;
import com.albom.iion.isr.processing.CoherentNoiseFinder;
import com.albom.iion.isr.processing.HeightIntegratorNum;
import com.albom.iion.isr.processing.TimeIntegratorSlide;

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

		CoherentNoiseFinder finder = new CoherentNoiseFinder(20, 4);
		TimeIntegratorSlide integrator = new TimeIntegratorSlide(20 * 60, 10 * 60);

		for (int lag = 0; lag < nLag; lag++) {

			for (int h = 0; h < nH; h++) {

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

	private void run(String[] args) {

		project = new ProjectFactory().getProject("d:/data.db3");

		if (project == null) {
			System.exit(-1);
		}

		load(Paths.get("d:/test"));

		getProperties();

		temporal();
		altitudinal();

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
