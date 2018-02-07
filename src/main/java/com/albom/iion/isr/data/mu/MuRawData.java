package com.albom.iion.isr.data.mu;

public class MuRawData extends MuData {

	private MuEachHeader eh;
	private double[] re = null;
	private double[] im = null;

	public MuRawData(MuEachHeader eh, double[] re, double[] im) {
		this.eh = eh;
		this.re = re;
		this.im = im;
	}

	/**
	 * @return the each header
	 */
	public MuEachHeader getEh() {
		return eh;
	}

	/**
	 * @return the real
	 */
	public double[] getRe() {
		return re;
	}

	/**
	 * @return the imaginary
	 */
	public double[] getIm() {
		return im;
	}

}
