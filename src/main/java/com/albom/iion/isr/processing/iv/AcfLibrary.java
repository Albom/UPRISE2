package com.albom.iion.isr.processing.iv;

public class AcfLibrary {

	private int num = 0;
	private double[] acfs = null;
	private final int LENGTH = 7;

	public AcfLibrary(int tMin, int tMax, int step) {
		acfs = new double[num * LENGTH];
	}

	private void calcNum(int tMin, int tMax, int step) {
		num = 0;
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

}
