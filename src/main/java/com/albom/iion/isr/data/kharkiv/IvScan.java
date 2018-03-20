package com.albom.iion.isr.data.kharkiv;

public class IvScan {
	private int type;
	private int[] re;
	private int[] im;

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the Re
	 */
	public int[] getRe() {
		return re;
	}

	/**
	 * @param re
	 *            the Re to set
	 */
	public void setRe(int[] re) {
		this.re = re;
	}

	/**
	 * @return the Im
	 */
	public int[] getIm() {
		return im;
	}

	/**
	 * @param im
	 *            the Im to set
	 */
	public void setIm(int[] im) {
		this.im = im;
	}
}