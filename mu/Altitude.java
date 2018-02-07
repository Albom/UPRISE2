package com.albom.iion.isr.processing.mu;

import com.albom.physics.Constants;

public abstract class Altitude {

	private static final int[] OFFSET = { 0, 0, -4, -1, 0, -1, 0 };

	public static int correct(int h, int lag, int step) {
		return h + OFFSET[lag]*step;
	}

	public static double getAbsolute(int h, int start, int sampling, int zenith) {
		return Constants.SPEED_OF_LIGHT * (start + sampling * h) * 1e-6 / 2.0 * Math.cos(Math.toRadians(zenith)) / 1000.0;
	}

}
