package com.albom.iion.isr.processing.mu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.albom.iion.isr.processing.ForwardProblem;
import com.albom.iion.isr.radars.MURadar;
import com.albom.physics.UnitPrefix;

public class AcfLibrary {
	
	private class Offset {
		
		private int ti = 0;
		private int te = 0;
		private int offset = 0;
		
		public Offset(int ti, int te, int offset){
			this.ti = ti;
			this.te = te;
			this.offset = offset;
		}

	}

	private final int LENGTH = 7;
	private int num = 0;

	private double[] acfs = null;

	private Map<Long, Offset> offsets = new HashMap<Long, Offset>();
	
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
				offsets.put((long) ti << 32 | (long) te, new Offset(ti, te, pos * LENGTH));
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
	 * @return the num
	 */
	public int getNum() {
		return num;
	}

	public boolean getAcf(int ti, int te, double acf[]) {
		Offset offset = offsets.get((long) ti << 32 | (long) te);
		if (offset == null) {
			return false;
		}
		for (int lag = 0; lag < LENGTH; lag++) {
			acf[lag] = acfs[lag + offset.offset];
		}
		return true;
	}

}
