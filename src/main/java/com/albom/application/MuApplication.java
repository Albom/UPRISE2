package com.albom.application;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import com.albom.iion.isr.data.Point;
import com.albom.iion.isr.data.ProjectDB;
import com.albom.iion.isr.data.ProjectFS;
import com.albom.iion.isr.data.ProjectFactory;
import com.albom.iion.isr.data.mu.MuProjectFS;
import com.albom.iion.isr.processing.CoherentNoiseFinder;
import com.albom.iion.isr.processing.ForwardProblem;
import com.albom.iion.isr.processing.HeightIntegratorNum;
import com.albom.iion.isr.processing.TimeIntegratorBlock;
import com.albom.iion.isr.processing.TimeIntegratorNum;
import com.albom.iion.isr.processing.TimeIntegratorSlide;
import com.albom.iion.isr.processing.mu.AcfLibrary;
import com.albom.iion.isr.processing.mu.Altitude;
import com.albom.iion.isr.radars.MURadar;
import com.albom.physics.UnitPrefix;
import com.albom.utils.PointLogger;
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

	private static final double[] time = { 0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1, 1.1, 1.2, 1.3, 1.4, 1.5,
			1.6, 1.7, 1.8, 1.9, 2, 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8, 2.9, 3, 3.1, 3.2, 3.3, 3.4, 3.5, 3.6, 3.7,
			3.8, 3.9, 4, 4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 4.7, 4.8, 4.9, 5, 5.1, 5.2, 5.3, 5.4, 5.5, 5.6, 5.7, 5.8, 5.9, 6,
			6.1, 6.2, 6.3, 6.4, 6.5, 6.6, 6.7, 6.8, 6.9, 7, 7.1, 7.2, 7.3, 7.4, 7.5, 7.6, 7.7, 7.8, 7.9, 8, 8.1, 8.2,
			8.3, 8.4, 8.5, 8.6, 8.7, 8.8, 8.9, 9, 9.1, 9.2, 9.3, 9.4, 9.5, 9.6, 9.7, 9.8, 9.9, 10, 10.1, 10.2, 10.3,
			10.4, 10.5, 10.6, 10.7, 10.8, 10.9, 11, 11.1, 11.2, 11.3, 11.4, 11.5, 11.6, 11.7, 11.8, 11.9, 12, 12.1,
			12.2, 12.3, 12.4, 12.5, 12.6, 12.7, 12.8, 12.9, 13, 13.1, 13.2, 13.3, 13.4, 13.5, 13.6, 13.7, 13.8, 13.9,
			14, 14.1, 14.2, 14.3, 14.4, 14.5, 14.6, 14.7, 14.8, 14.9, 15, 15.1, 15.2, 15.3, 15.4, 15.5, 15.6, 15.7,
			15.8, 15.9, 16, 16.1, 16.2, 16.3, 16.4, 16.5, 16.6, 16.7, 16.8, 16.9, 17, 17.1, 17.2, 17.3, 17.4, 17.5,
			17.6, 17.7, 17.8, 17.9, 18, 18.1, 18.2, 18.3, 18.4, 18.5, 18.6, 18.7, 18.8, 18.9, 19, 19.1, 19.2, 19.3,
			19.4, 19.5, 19.6, 19.7, 19.8, 19.9, 20, 20.1, 20.2, 20.3, 20.4, 20.5, 20.6, 20.7, 20.8, 20.9, 21, 21.1,
			21.2, 21.3, 21.4, 21.5, 21.6, 21.7, 21.8, 21.9, 22, 22.1, 22.2, 22.3, 22.4, 22.5, 22.6, 22.7, 22.8, 22.9,
			23, 23.1, 23.2, 23.3, 23.4, 23.5, 23.6, 23.7, 23.8, 23.9 };

	private static final double[] ti_iri_200 = { 764.4, 759, 758.6, 758.2, 757.8, 757.5, 757.2, 756.9, 756.6, 756.3,
			756, 755.7, 755.4, 755.1, 754.8, 754.4, 754.1, 753.7, 753.3, 752.9, 752.5, 752, 751.5, 751, 750.5, 749.9,
			749.3, 748.6, 748, 747.3, 746.6, 737.2, 736.5, 735.8, 735.1, 734.4, 733.7, 733, 732.4, 731.7, 731.1, 730.4,
			729.8, 729.3, 728.8, 728.3, 727.9, 727.6, 727.3, 727, 726.9, 726.8, 726.9, 727, 727.2, 727.5, 727.9, 728.4,
			729, 729.7, 730.5, 739.8, 740.8, 741.9, 743.1, 744.4, 745.9, 747.4, 749.1, 750.8, 752.6, 754.5, 756.6,
			758.6, 760.8, 763, 765.3, 767.7, 770.1, 772.6, 775.1, 777.6, 780.2, 782.8, 785.5, 788.1, 790.7, 793.4,
			796.1, 798.7, 801.4, 801.1, 803.7, 806.3, 808.9, 811.4, 813.9, 816.4, 818.8, 821.2, 823.6, 825.9, 828.1,
			830.4, 832.5, 834.6, 836.7, 838.7, 840.7, 842.6, 844.4, 846.2, 848, 849.7, 851.3, 852.9, 854.4, 855.9,
			857.3, 858.7, 860, 859.6, 860.8, 861.9, 863, 864, 865, 865.9, 866.8, 867.5, 868.3, 868.9, 869.5, 869.9,
			870.4, 870.7, 870.9, 871.1, 871.1, 871.1, 871, 870.8, 870.5, 870, 869.5, 868.9, 868.2, 867.4, 866.4, 865.4,
			864.3, 861.6, 860.3, 858.9, 857.4, 855.9, 854.2, 852.5, 850.8, 849, 847.2, 845.3, 843.4, 841.5, 839.6,
			837.8, 835.9, 834, 832.2, 830.4, 828.7, 827, 825.4, 823.9, 822.4, 820.9, 819.6, 818.3, 817.1, 816, 814.9,
			816.9, 816, 815.1, 814.3, 813.5, 812.8, 812.1, 811.4, 810.8, 810.1, 809.5, 808.8, 808.2, 807.5, 806.8, 806,
			805.3, 804.4, 803.6, 802.7, 801.7, 800.7, 799.6, 798.5, 797.4, 796.2, 794.9, 793.6, 792.3, 791, 794.3, 793,
			791.6, 790.3, 788.9, 787.5, 786.2, 784.8, 783.5, 782.2, 780.9, 779.6, 778.4, 777.2, 776, 774.9, 773.8,
			772.8, 771.8, 770.9, 770.1, 769.2, 768.5, 767.8, 767.1, 766.5, 765.9, 765.3, 764.8 };

	private static final double[] ti_iri_250 = { 788.5, 782, 781.4, 780.9, 780.4, 780, 779.6, 779.3, 779, 778.7, 778.4,
			778.2, 778, 777.8, 777.6, 777.5, 777.3, 777.1, 777, 776.8, 776.6, 776.4, 776.2, 776, 775.8, 775.6, 775.4,
			775.1, 774.9, 774.6, 774.4, 763.4, 763.2, 762.9, 762.7, 762.5, 762.3, 762.1, 761.9, 761.7, 761.6, 761.4,
			761.4, 761.3, 761.3, 761.4, 761.5, 761.6, 761.8, 762.1, 762.4, 762.8, 763.3, 763.9, 764.5, 765.3, 766.1,
			767, 768, 769.1, 770.3, 782.4, 783.7, 785.2, 786.7, 788.3, 790, 791.8, 793.6, 795.6, 797.6, 799.7, 801.9,
			804.1, 806.4, 808.7, 811.1, 813.6, 816.1, 818.6, 821.1, 823.7, 826.3, 828.9, 831.5, 834.2, 836.8, 839.4,
			842, 844.6, 847.2, 845.6, 848.2, 850.7, 853.2, 855.7, 858.2, 860.6, 863, 865.3, 867.6, 869.9, 872.1, 874.3,
			876.5, 878.6, 880.7, 882.7, 884.8, 886.7, 888.7, 890.6, 892.4, 894.3, 896.1, 897.9, 899.6, 901.3, 903,
			904.7, 906.4, 906, 907.6, 909.2, 910.8, 912.3, 913.8, 915.3, 916.8, 918.2, 919.6, 921, 922.3, 923.6, 924.8,
			926.1, 927.2, 928.3, 929.4, 930.3, 931.3, 932.1, 932.9, 933.6, 934.2, 934.7, 935.1, 935.4, 935.7, 935.8,
			935.8, 934.1, 933.9, 933.6, 933.3, 932.8, 932.2, 931.5, 930.7, 929.8, 928.8, 927.8, 926.7, 925.5, 924.2,
			922.9, 921.6, 920.2, 918.8, 917.3, 915.9, 914.4, 912.9, 911.4, 909.9, 908.3, 906.8, 905.3, 903.8, 902.2,
			900.7, 903.5, 901.9, 900.4, 898.8, 897.2, 895.6, 893.9, 892.2, 890.5, 888.6, 886.8, 884.8, 882.8, 880.7,
			878.6, 876.3, 874, 871.7, 869.2, 866.7, 864.1, 861.5, 858.8, 856.1, 853.3, 850.5, 847.6, 844.8, 841.9, 839,
			842.2, 839.4, 836.7, 833.9, 831.2, 828.6, 826, 823.4, 820.9, 818.5, 816.2, 813.9, 811.7, 809.6, 807.6,
			805.7, 803.9, 802.2, 800.5, 799, 797.6, 796.2, 795, 793.8, 792.7, 791.7, 790.8, 789.9, 789.2 };

	private void load(Path directory) {
		project.createTable(step1);
		ProjectFS projectFS = new MuProjectFS(project, step1);
		projectFS.load(directory);
	}

	private void temporal() {

		project.createTable(step2);

		CoherentNoiseFinder finder = new CoherentNoiseFinder(20, 4.0);

		TimeIntegratorNum integratorNum = new TimeIntegratorNum(18, 18);

		TimeIntegratorSlide integrator = new TimeIntegratorSlide(3 * 60 * 60, 15 * 60);

		for (int lag = 0; lag < nLag; lag++) {

			System.out.println("lag=" + lag);

			for (int h = 0; h < nH; h++) {

				System.out.println("h=" + h);

				List<Point> points = project.getTimeDependency(step1, h, lag);
				points = integratorNum.integrate(points);

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

			HeightIntegratorNum heigthIntegrator = new HeightIntegratorNum(3);

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

		final int tMax = 3000;
		final int tMin = 600;
		AcfLibrary library = new AcfLibrary(tMin, tMax, 10);
		double[] acfTheor = new double[7];

		LinearInterpolator interpolator = new LinearInterpolator();
		PolynomialSplineFunction psf = interpolator.interpolate(time, ti_iri_250);

		List<LocalDateTime> dates = project.getDates(step3, 1);
		for (LocalDateTime date : dates) {
			for (int h = 0; h < nH; h++) {
				double altitude = Altitude.getAbsolute(h, start, sampling, zenith);
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
							// int ti = (((int) psf.value(Time.toDecimal(date)))
							// / 10) * 10;
							for (int te = ti; te <= tMax; te += 10) {
								library.getAcf(ti, te, acfTheor);
								double d = 0;
								for (int lag = 1; lag <= 6; lag++) {
									// if (lag != 2) {
									d += Math.pow(acf[lag] - acfTheor[lag] * acf[0], 2);
									// }
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
					System.out.print(String.format(Locale.US, "%8.5f\t%8.1f\t%5d\t%5d\t%7.2f\t%7.3E\t\t",
							Time.toDecimal(date) - 1.5, altitude, tiEstimated, teEstimated, kEstimated, deltaMin));
					acf[0] = acf[1] * kEstimated;
					for (int lag = 0; lag <= 6; lag++) {
						System.out.print(String.format(Locale.US, "%8.4f\t", acf[lag]));
					}
					System.out.print("\t");

					library.getAcf(tiEstimated, teEstimated, acfTheor);
					for (int lag = 0; lag <= 6; lag++) {
						System.out.print(String.format(Locale.US, "%8.4f\t", acfTheor[lag] * acf[0]));
					}

					System.out.println(" " + h);

					// break;
				}
			}
			// System.out.println();
			// break;
		}
	}

	private void snr() {
		int lag = 0;
		List<LocalDateTime> dates = project.getDates(step3, lag);
		
		System.out.print("0\t");
		for (int i = 0; i < nH; i++){
			System.out.print(Altitude.getAbsolute(i, start, sampling, zenith)+"\t");
		}
		System.out.println();
		
		for (LocalDateTime date : dates) {
			List<Point> points = project.getHeightDependency(step3, date, lag);

//			int h = 0;
//			int c = 0;
//			double pN = 0;
//			for (Point p : points) {
//				if (Altitude.getAbsolute(h++, start, sampling, zenith) > 400) {
//					pN += p.getValue();
//					c++;
//				}
//			}
//			pN /= c;


			 
			int h = 0;
			double pN = Double.MAX_VALUE;
			for (Point p : points) {
				if (Altitude.getAbsolute(h++, start, sampling, zenith) > 400) {
					if ( p.getValue() < pN) {
						pN = p.getValue();
					}
				}
			}

			
			System.out.print( String.valueOf((Time.toDecimal(date)-1.5)) + "\t");

			for (Point p : points) {
				System.out.print(String.valueOf((p.getValue() - pN) / pN) + "\t");

			}
			System.out.println();

		}
	}

	private void run(String[] args) {

		project = new ProjectFactory().getProject("d:/data.db3");

		if (project == null) {
			System.exit(-1);
		}

		// load(Paths.get("d:/test/"));

		getProperties();

		// temporal();
		// altitudinal();
		// inverse();
		snr();

		project.close();

	}

	private void test() {
		AcfLibrary library = new AcfLibrary(600, 2000, 10);
		double[] acfTheor = new double[7];
		for (int ti = 600; ti < 2000; ti += 10) {
			for (int te = ti; te < 2000; te += 10) {
				library.getAcf(ti, te, acfTheor);

				System.out.print(ti + "\t" + te + "\t");
				System.out.println(
						Arrays.toString(acfTheor).replaceAll("\\[", "").replaceAll("\\[", "").replaceAll(",", "\t"));

			}
		}
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
		// app.test();
	}

}
