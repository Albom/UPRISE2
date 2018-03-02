package com.albom.iion.isr.processing.mu;

import java.time.LocalDateTime;
import java.util.LinkedList;

import com.albom.iion.isr.data.Point;
import com.albom.iion.isr.data.mu.MuHeader;
import com.albom.iion.isr.data.mu.MuPulse;
import com.albom.iion.isr.data.mu.MuRawData;
import com.albom.iion.isr.data.mu.MuSession;
import com.albom.iion.isr.radars.MURadar;

public class Acf {

	private Acf() {
	}

	private static LinkedList<Point> calcPower(MuSession s) {
		LinkedList<Point> points = new LinkedList<>();
		MuHeader head = s.getHead();
		LocalDateTime date = head.getRecsta();
		MuRawData[] data = (MuRawData[]) s.getData();

		for (int h = 0; h < data.length; h++) {
			double powerRe = 0;
			double powerIm = 0;
			double biasRe = 0;
			double biasIm = 0;
			double[] re = data[h].getRe();
			double[] im = data[h].getIm();
			int nT = re.length;
			for (int t = 0; t < nT; t++) {
				powerRe += re[t] * re[t];
				powerIm += im[t] * im[t];
				biasRe += re[t];
				biasIm += im[t];
			}
			points.add(new Point(date, h, 0, powerRe - biasRe * biasRe / nT + powerIm - biasIm * biasIm / nT));
		}
		return points;
	}

	private static LinkedList<Point> calcAcf(MuSession s) {
		LinkedList<Point> points = new LinkedList<>();
		MuHeader head = s.getHead();
		LocalDateTime date = head.getRecsta();
		MuRawData[] data = (MuRawData[]) s.getData();

		int nT = head.getNdata();
		int nH = head.getNhigh();

		double[][] re = new double[nH][nT];
		double[][] im = new double[nH][nT];

		for (int h = 0; h < nH; h++) {
			double[] tRe = data[h].getRe();
			double[] tIm = data[h].getIm();
			for (int t = 0; t < nT; t++) {
				re[h][t] = tRe[t];
				im[h][t] = tIm[t];
			}
		}

		int step = MURadar.DISTANCE_BETWEEN_PULSES / head.getJsint();

		for (int tau = 1; tau <= 6; tau++) {
			for (int h = 0; h < nH - tau * step; h++) {
				double acfRe = 0;
				double acfIm = 0;
				double biasRe = 0;
				double biasIm = 0;
				double biasReTau = 0;
				double biasImTau = 0;
				for (int t = 0; t < nT; t++) {
					acfRe += re[h][t] * re[h + tau * step][t];
					acfIm += im[h][t] * im[h + tau * step][t];
					biasRe += re[h][t];
					biasIm += im[h][t];
					biasReTau += re[h + tau * step][t];
					biasImTau += im[h + tau * step][t];
				}
				int altitude = Altitude.correct(h, tau, step);
				if ((altitude >= 0) && (altitude < nH)) {
					points.add(new Point(date, altitude, tau,
							acfRe - biasRe * biasReTau / nT + acfIm - biasIm * biasImTau / nT));
				}

			}
		}

		return points;
	}

	public static LinkedList<Point> calc(MuSession s) {

		int pattern = s.getHead().getMpulse().getPattern();
		if (pattern == MuPulse.ONE_PULSE) {
			return calcPower(s);
		} else if (pattern == MuPulse.FOUR_PULSES) {
			return calcAcf(s);
		} else {
			return new LinkedList<>();
		}

	}

}
