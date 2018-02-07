package com.albom.iion.isr.data.mu;

public class MuPulse {

	public static final int ONE_PULSE = 0;
	public static final int FOUR_PULSES = 0x53;
	
	private int pattern = 0;

	public MuPulse(int pattern) {
		this.setPattern(pattern);
	}

	public int getPattern() {
		return pattern;
	}

	private void setPattern(int pattern) {
		this.pattern = pattern;
	}

	@Override
	public String toString() {
		return new StringBuilder(Integer.toBinaryString(pattern)).reverse().toString() ;
	}
}
