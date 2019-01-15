package com.albom.iion.isr.radars;

import com.albom.physics.Constants;

public final class KharkivRadar {

	public final static double FREQUENCY = 158.0036e6;
	public final static double WAVE_LENGTH = Constants.SPEED_OF_LIGHT / FREQUENCY;

	public final static double LATITUDE = 49.676;
	public final static double LONGITUDE = 36.292;

	public final static double INVARIANT_LATITUDE = 45.74;

	public final static double MODE_IV_SAMPLING_RATE = 40.0; // us
	public final static double MODE_12_SAMPLING_RATE = 30.555; // us

	public final static int[] MODE_IV_LAGS = { 0, 7, 8, 9, 10, 11, 12 };
	
	private KharkivRadar() {
	}

	public static double[] getArresterIvMode() {
		final int NH4 = 2500;
		double[] r = new double[NH4];
		for (int ih4 = 0; ih4 < NH4; ih4++) {
			if (ih4 < 187) {
				r[ih4] = 0;
			} else if (ih4 >= 600) {
				r[ih4] = 1;
			} else {
				r[ih4] = 2.306279e-15 * Math.pow(ih4, 6) - 5.0407075e-12 * Math.pow(ih4, 5)
						+ 4.2045693e-9 * Math.pow(ih4, 4) - 1.592397e-6 * Math.pow(ih4, 3)
						+ 2.1625741e-4 * Math.pow(ih4, 2) + 0.0188536 * ih4 - 4.7499283;
			}
		}
		return r;
	}

}
