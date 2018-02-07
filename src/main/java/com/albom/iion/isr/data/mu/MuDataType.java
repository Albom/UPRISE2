package com.albom.iion.isr.data.mu;

import java.util.HashMap;

public class MuDataType {

	public static enum Mode {
		FFT, ACF
	}

	protected static final HashMap<Integer, String> mapFft;
	protected static final HashMap<Integer, String> mapAcf;
	static {
		mapFft = new HashMap<Integer, String>();
		mapFft.put(Integer.valueOf(0), "spectra only");
		mapFft.put(Integer.valueOf(1), "spectra & parameters");
		mapFft.put(Integer.valueOf(2), "spectra & parameters & power");
		mapFft.put(Integer.valueOf(11), "parameters only (real-time)");
		mapFft.put(Integer.valueOf(12), "parameters (real-time) & power");
		mapFft.put(Integer.valueOf(21), "parameters only (off-line)");
		mapFft.put(Integer.valueOf(22), "parameters (off-line) & power");

		mapAcf = new HashMap<Integer, String>();
		mapAcf.put(Integer.valueOf(1), "ACF & power & DC component");
		mapAcf.put(Integer.valueOf(2), "power & DC component");
		mapAcf.put(Integer.valueOf(3), "ACF only");
	}

	protected int type;

	public MuDataType(int type) {
		setType(type);
	}

	public int getType() {
		return type;
	}

	private void setType(int type) {
		this.type = type;
	}

	public String getDescription(Mode mode) {
		switch (mode) {
		case FFT:
			return mapFft.get(type);
		case ACF:
			return mapAcf.get(type);
		default:
			return "Error! Unknown type.";

		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + type + "]";
	}

}
