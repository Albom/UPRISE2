package com.albom.iion.isr.processing.iv;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import com.albom.iion.isr.data.Point;
import com.albom.iion.isr.data.kharkiv.IvFile;
import com.albom.iion.isr.data.kharkiv.IvScan;
import com.albom.iion.isr.radars.KharkivRadar;

public class Acf {

	private Acf() {
	}

	/**
	 * 
	 * @param file
	 *            iv file
	 * @return points
	 */
	public static List<Point> calc(IvFile file) {
		LinkedList<Point> points = new LinkedList<>();

		LocalDateTime date = file.getDate();

		final int nT = file.getNumScan();
		final int nH = file.getNumPoint();
		final int nLag = 7;

		final int step = (int) (KharkivRadar.MODE_IV_SAMPLING_RATE) / file.getInterval();

		List<IvScan> scans = file.getScans();

		double[][] acf = new double[nH][nLag];
		double[][] biasRe = new double[nH][nLag];
		double[][] biasIm = new double[nH][nLag];
		for (int t = 0; t < nT; t++) {

			for (int tau = 0; tau < nLag; tau++) {

				int[] re = scans.get(t * nLag + tau).getRe();
				int[] im = scans.get(t * nLag + tau).getIm();

				for (int h = KharkivRadar.MODE_IV_LAGS[tau] * step; h < nH; h++) {
					int corrH = h - KharkivRadar.MODE_IV_LAGS[tau] * step; 
					acf[h][tau] += re[h] * re[corrH] + im[h] * im[corrH];
					biasRe[h][tau] += re[h];
					biasIm[h][tau] += im[h];

				}
			}
		}

		for (int tau = 0; tau < nLag; tau++) {
			for (int h = KharkivRadar.MODE_IV_LAGS[tau] * step; h < nH; h++) {
				int corrH = h - KharkivRadar.MODE_IV_LAGS[tau] * step; 
				points.add(new Point(date, h, KharkivRadar.MODE_IV_LAGS[tau],
						acf[h][tau] 
								- biasRe[h][tau] * biasRe[corrH][tau] / nT
								- biasIm[h][tau] * biasIm[corrH][tau] / nT
								));
			}
		}

		return points;
	}

	
}
