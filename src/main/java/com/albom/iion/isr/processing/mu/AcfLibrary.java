package com.albom.iion.isr.processing.mu;

import java.util.HashMap;
import java.util.Map;

import com.albom.iion.isr.processing.ForwardProblem;
import com.albom.iion.isr.radars.MURadar;
import com.albom.physics.UnitPrefix;

public class AcfLibrary {

	private class Offsets {

		Map<Long, Integer> map = new HashMap<Long, Integer>();

		private void put(int ti, int te, Integer offset) {
			map.put((long) ti << 32 | (long) te, offset);
		}

		private Integer get(int ti, int te) {
			return map.get((long) ti << 32 | (long) te);
		}

	}

	private final int LENGTH = 7;
	private int num = 0;

	private double[] acfs = null;

	Offsets offsets = new Offsets();

	public AcfLibrary(int tMin, int tMax, int step) {

		calcNum(tMin, tMax, step);
		acfs = new double[num * LENGTH];

		ForwardProblem forward = new ForwardProblem(MURadar.WAVE_LENGTH);
		double[] acf = new double[LENGTH];
		final double DELTA_TAU = UnitPrefix.micro(MURadar.DISTANCE_BETWEEN_PULSES);

		int pos = 0;
		for (int ti = tMin; ti <= tMax; ti += step) {
			for (int te = ti; te <= tMax; te += step) {
				forward.acf(ti, te, DELTA_TAU, acf);
				for (int lag = 0; lag < LENGTH; lag++) {
					acfs[lag + pos * LENGTH] = acf[lag];
				}
				offsets.put(ti, te, pos * LENGTH);
				pos++;
			}
		}

	}

	private void calcNum(int tMin, int tMax, int step) {
		for (int ti = tMin; ti <= tMax; ti += step) {
			for (int te = ti; te <= tMax; te += step) {
				num++;
			}
		}
	}

	/**
	 * @return num
	 */
	public int getNum() {
		return num;
	}

	public boolean getAcf(int ti, int te, double acf[]) {
		Integer offset = offsets.get(ti << 32, te);
		if (offset == null) {
			return false;
		}
		for (int lag = 0; lag < LENGTH; lag++) {
			acf[lag] = acfs[lag + offset];
		}
		return true;
	}

}
