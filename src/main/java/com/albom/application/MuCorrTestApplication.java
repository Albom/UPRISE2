package com.albom.application;

import java.util.List;

import com.albom.iion.isr.data.Point;
import com.albom.iion.isr.data.mu.MuPulse;
import com.albom.iion.isr.data.mu.MuRawData;
import com.albom.iion.isr.data.mu.MuSession;
import com.albom.iion.isr.data.mu.MuSessionFS;
import com.albom.iion.isr.processing.mu.Acf;
import com.albom.utils.Directory;

public class MuCorrTestApplication {

	private void test1() {

		final int nH = 200;
		final int nLag = 48;

		double[][] cor = new double[nLag][nH];

		final String dir = "d:/test/";
		List<String> files = Directory.list(dir);

		for (String file : files) {
			System.out.println(file);

			for (int i = 0;; i++) {
				MuSession s = MuSessionFS.load(file, i);
				if (s == null) {
					break;
				}
				if (s.getHead().getMpulse().getPattern() == MuPulse.FOUR_PULSES) {

					MuRawData[] data = (MuRawData[]) s.getData();

					for (int lag = 0; lag < nLag; lag++) {

						for (int h = 0; h < nH; h++) {

							double acfRe = 0;
							double biasRe = 0;
							double biasReTau = 0;

							for (int t = 0; t < 512; t++) {
								cor[lag][h] += data[h].getRe()[t] * data[h + lag].getRe()[t];
								biasRe += data[h].getRe()[t];
								biasReTau += data[h + lag].getRe()[t];
							}

							cor[lag][h] += acfRe - biasRe * biasReTau / 512.0;

						}

					}

				}
			}

		}

		for (int lag = 0; lag < nLag; lag++) {
			for (int h = 0; h < nH - nLag; h++) {
				System.out.println(lag * 32.0 / 192.0 + "\t" + h + "\t" + cor[lag][h]);
			}
			System.out.println();
		}
	}


	private void test2() {

		final int nH = 200;
		final int nLag = 7;

		double[][] cor = new double[nLag][nH];

		final String dir = "d:/test/";
		List<String> files = Directory.list(dir);

		for (String file : files) {
			System.out.println(file);

			for (int i = 0;; i++) {
				MuSession s = MuSessionFS.load(file, i);
				if (s == null) {
					break;
				}

				List<Point> points = Acf.calc(s);
				for (Point p : points) {
					if (p.getAlt() < nH) {
						cor[p.getLag()][p.getAlt()] += p.getValue();
					}
				}

			}

		}

		System.out.print(0 + "\t");
		for (int lag = 0; lag < nLag; lag++) {
			System.out.print(lag + "\t");
		}

		System.out.println();

		for (int h = 0; h < nH - nLag; h++) {
			System.out.print(h + "\t");
			for (int lag = 0; lag < nLag; lag++) {
				System.out.print(cor[lag][h] + "\t");
			}
			System.out.println();
		}

	}

	public static void main(String[] args) {

		MuCorrTestApplication app = new MuCorrTestApplication();
		app.test2();

	}

}
