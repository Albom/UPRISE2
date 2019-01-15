package com.albom.iion.isr.processing.iv;

public abstract class Altitude {

	public static double getAbsolute(int h) {
		return -120 + 1.2 * h;
	}

}
