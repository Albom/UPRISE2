package com.albom.application;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.albom.iion.isr.data.Point;
import com.albom.iion.isr.data.ProjectDB;
import com.albom.iion.isr.data.ProjectFS;
import com.albom.iion.isr.data.ProjectFactory;
import com.albom.iion.isr.data.kharkiv.IvProjectFS;
import com.albom.iion.isr.processing.CoherentNoiseFinder;
import com.albom.iion.isr.processing.ForwardProblem;
import com.albom.iion.isr.processing.HeightIntegratorNum;
import com.albom.iion.isr.processing.InverseProblem;
import com.albom.iion.isr.processing.TimeIntegratorSlide;
import com.albom.iion.isr.processing.iv.Altitude;
import com.albom.iion.isr.radars.KharkivRadar;
import com.albom.physics.IonMass;

public class IvApplication {

	private ProjectDB project;
	private final String step1 = "step1";
	private final String step2 = "step2";
	private final String step3 = "step3";
	private final String step4 = "step4";
	// private final int[] LAGS = { 0, 7, 8, 9, 10, 11, 12 };

	private int nH;

	private void run() {

		// projectOpen(Paths.get("e:/IV_2017_12_26 (15 мин, без
		// разрядника).db3"));
		projectOpen(Paths.get("e:/IV_2018_11_21.db3"));

//		 load(Paths.get("h:/Data/21-11-2018/"));

		getProperties();

//		 temporal();

//		altitudinal();

//		 subNoise();

//		 exportStep1Step2();
//		 exportStep2Step3();
//		 exportStep4();

		 inverse();
//		 snr();

		// noiseACF();

	}

	private void noiseACF() {
		double[] acfNoise = new double[7];
		List<LocalDateTime> dates = project.getDates(step4, 0);
		for (LocalDateTime date : dates) {
			for (int lag = 0; lag < 7; lag++) {
				List<Point> profile = project.getHeightDependency(step2, date, KharkivRadar.MODE_IV_LAGS[lag]);
				for (int h = 1500; h < 2200; h++) {
					acfNoise[lag] += profile.get(h).getValue();
				}
			}
		}
		for (int lag = 1; lag < 7; lag++) {
			System.out.println(KharkivRadar.MODE_IV_LAGS[lag] * 40 + "\t" + acfNoise[lag] / acfNoise[0]);
		}
	}

	private void load(Path directory) {
		project.createTable(step1);
		ProjectFS projectFS = new IvProjectFS(project, step1);
		projectFS.load(directory);
	}

	private void temporal() {
		project.createTable(step2);

		CoherentNoiseFinder finder = new CoherentNoiseFinder(15, 4.5);
		TimeIntegratorSlide integrator = new TimeIntegratorSlide(30 * 60, 1 * 60);

		for (int lag : KharkivRadar.MODE_IV_LAGS) {

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
		HeightIntegratorNum heigthIntegrator = new HeightIntegratorNum(5);
		for (int lag : KharkivRadar.MODE_IV_LAGS) {

			System.out.println("lag=" + lag);

			double[] razr = KharkivRadar.getArresterIvMode();
			List<LocalDateTime> dates = project.getDates(step2, lag);
			for (LocalDateTime d : dates) {
				List<Point> profileIn = project.getHeightDependency(step2, d, lag);

				List<Point> profileOut = new ArrayList<>();
				for (int h = 0; h < profileIn.size(); h++) {
					// TODO replace 5 with a calculation of this value
					Point pIn = profileIn.get(h);
					int alt = pIn.getAlt();
					int corrH = alt - lag * 5;
					double value = 0;
					if ((alt >= 0) && (corrH >= 0) && (razr[h] * razr[corrH] > 1e-6)) {
						value = pIn.getValue() / Math.sqrt(razr[h] * razr[corrH]);
					}
					Point pOut = new Point(pIn.getDate(), alt, pIn.getLag(), value);
					profileOut.add(pOut);
				}

				List<Point> points = heigthIntegrator.integrate(profileOut);
//				List<Point> points = heigthIntegrator.integrate(profileIn);

				// List<Point> points_out = new ArrayList<>();

				// for (Point p : points) {
				//
				// TODO replace 5 with a calculation of this value
				// int step = 5; // a dirty hack
				//
				// int altitude = p.getAlt() - p.getLag() * step;
				//
				// if (altitude >= 0) {
				// points_out.add(new Point(p.getDate(), altitude, p.getLag(),
				// p.getValue()));
				// }
				//
				// if (lag > 0){
				// System.out.println(altitude);
				// }

				// }

				project.insert(step3, points);
			}

		}

	}

	private void getProperties() {
		nH = Integer.valueOf(project.getProperty("nh"));
	}

	private void subNoise() {
		project.createTable(step4);
		for (int lag : KharkivRadar.MODE_IV_LAGS) {
			List<LocalDateTime> dates = project.getDates(step3, lag);
			for (LocalDateTime d : dates) {
				List<Point> points = project.getHeightDependency(step3, d, lag);
				// if (lag == KharkivRadar.MODE_IV_LAGS[0]) {

				double noise = 0;
				int num = 0;
				for (int h = nH - 600; h <= nH - 300; h++) {
					noise += points.get(h).getValue();
					num++;
				}
				noise /= num;

				for (Point p : points) {
					p.setValue(p.getValue() - noise);
				}
				// }
				project.insert(step4, points);
			}
		}
	}

	private void exportStep1Step2() {

		int lag = KharkivRadar.MODE_IV_LAGS[0];
		List<Point> points = project.getTimeDependency(step1, 350, lag);
		StringBuilder builder = new StringBuilder();
		for (Point p : points) {
			builder.append(p.getDate() + ";" + String.valueOf(p.getValue()) + "\n");
		}
		String text = builder.toString();
		try {
			java.nio.file.Files.write(Paths.get("e:/step1_300km.txt"), text.getBytes("utf-8"),
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		points = project.getTimeDependency(step2, 350, lag);
		builder = new StringBuilder();
		for (Point p : points) {
			builder.append(p.getDate() + ";" + String.valueOf(p.getValue()) + "\n");
		}
		text = builder.toString();
		try {
			java.nio.file.Files.write(Paths.get("e:/step2_300km.txt"), text.getBytes("utf-8"),
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void projectOpen(Path path) {
		project = new ProjectFactory().getProject(path.toString());

		if (project == null) {
			System.out.println("Error opening project.");
			System.exit(-1);
		}
	}

	private void exportStep2Step3() {

		int lag = KharkivRadar.MODE_IV_LAGS[0];
		List<LocalDateTime> dates = project.getDates(step2, lag);

		for (LocalDateTime d : dates) {
			List<Point> points_step2 = project.getHeightDependency(step2, d, lag);
			List<Point> points_step3 = project.getHeightDependency(step3, d, lag);
			List<Point> points_step4 = project.getHeightDependency(step4, d, lag);

			StringBuilder builder = new StringBuilder();

			for (int i = 0; i < points_step2.size(); i++) {
				builder.append(Altitude.getAbsolute(i) + ";" + String.valueOf(points_step2.get(i).getValue()) + ";"
						+ String.valueOf(points_step3.get(i).getValue()) + ";"
						+ String.valueOf(points_step4.get(i).getValue()) + "\n");
			}
			String text = builder.toString();
			try {
				java.nio.file.Files.write(Paths.get("e:/" + d.toString().replaceAll(":", "-") + ".csv"),
						text.getBytes("utf-8"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void exportStep4() {
		
		
//		project = new ProjectFactory().getProject("e:/IV_2017_12_26.db3");
//
//		if (project == null) {
//			System.out.println("Error opening project.");
//			System.exit(-1);
//		}
//
//		getProperties();

		int lag = KharkivRadar.MODE_IV_LAGS[0];
		List<LocalDateTime> dates = project.getDates(step4, lag);

		StringBuilder builder = new StringBuilder();
		for (LocalDateTime d : dates) {
			builder.append(d.toString());
			List<Point> points = project.getAcf(step4, d, 350);
			for (Point p : points) {
				builder.append(";" + p.getValue());
			}
			builder.append("\n");

		}

		String text = builder.toString();
		try {
			java.nio.file.Files.write(Paths.get("e:/" + "300km.csv"), text.getBytes("utf-8"), StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void inverse() {

		final double deltaTau = KharkivRadar.MODE_IV_SAMPLING_RATE * 1e-6;
		final int LEN = 13;
		double[] acfExp = new double[LEN];

		ForwardProblem forward = new ForwardProblem(IonMass.HYDROGEN, IonMass.HELIUM, IonMass.OXYGEN);
		//ForwardProblem forward = new ForwardProblem(IonMass.OXYGEN, IonMass.OXYGEN, IonMass.HEAVY_MOLECULAR);

//		InverseProblem inverseProblem = new InverseProblem(forward, deltaTau);

		int lag = KharkivRadar.MODE_IV_LAGS[0];
		List<LocalDateTime> dates = project.getDates(step4, lag);

		StringBuilder builder = new StringBuilder();
		for (LocalDateTime d : dates) {
			int h = 266; // 266 - 200 km, 310 - 250 km
			List<Point> points = project.getAcf(step4, d, h);
			// for (int h = 200; h < 300; h += 10/**/) {
			// List<Point> points = project.getAcf(step4, dates.get(50), h);

			lag = 0;
			for (Point p : points) {
				acfExp[KharkivRadar.MODE_IV_LAGS[lag++]] = p.getValue();
			}
//			 System.out.println(Arrays.toString(acfExp).replaceAll("\\[",
//			 "").replaceAll("\\]", "").replaceAll(", ", "\t"));
			// double[] t = inverseProblem.find(acfExp);
			// System.out.println(Arrays.toString(t));
			double[] acfTheor = new double[LEN];
			int ti_c = 0, te_c = 0;
			double delta_min = Double.MAX_VALUE;
			for (int ti = 500; ti <= 3000; ti += 10) {
				for (int te = ti; te <= 3000; te += 10) {
					forward.acf(ti, te, deltaTau, acfTheor);
					double delta = 0;
					for (lag = 7; lag < LEN; lag++) {
						delta += (acfTheor[lag] * acfExp[0] - acfExp[lag]) * (acfTheor[lag] * acfExp[0] - acfExp[lag]);
					}
					if (delta < delta_min) {
						delta_min = delta;
						ti_c = ti;
						te_c = te;
					}
				}
			}

			forward.acf(ti_c, te_c, deltaTau, acfTheor);
			for (int i = 0; i < acfTheor.length; i++) {
				acfTheor[i] *= acfExp[0];
			}
//			 System.out.println(Arrays.toString(acfTheor).replaceAll("\\[",
//			 "").replaceAll("\\]", "").replaceAll(", ", "\t"));

			System.out.println(d.toString() + "\t" + ti_c + "\t" + te_c);
//			 System.out.println();
		}

	}

	private void snr() {
		List<LocalDateTime> dates = project.getDates(step4, 0);
		StringBuilder temp = new StringBuilder();
		for (LocalDateTime d : dates) {
			int alt = 1500;
			List<Point> powerIS = project.getHeightDependency(step4, d, 0);
			List<Point> acf = project.getAcf(step3, d, alt);
			double powerNoise = acf.get(0).getValue() - powerIS.get(alt).getValue();
			for (Point p : powerIS) {
				temp.append(d.toString() + ";" + Double.valueOf(p.getAlt() * 1.2 - 120) + ";"
						+ p.getValue() / powerNoise + "\n");
			}
			temp.append("\n");
		}
		String s = temp.toString();
		try {
			java.nio.file.Files.write(Paths.get("e:/snr.csv"), s.getBytes("utf-8"), StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		IvApplication app = new IvApplication();
		app.run();

	}

}
