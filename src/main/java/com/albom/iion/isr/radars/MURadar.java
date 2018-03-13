package com.albom.iion.isr.radars;

import com.albom.physics.Constants;

public class MURadar {

	public final static double FREQUENCY = 46.5e6;
	public final static double WAVE_LENGTH = Constants.SPEED_OF_LIGHT / FREQUENCY;

	public final static int DISTANCE_BETWEEN_PULSES = 192; // us
	
	public final static double LATITUDE = 34.8;
	public final static double LONGITUDE = 136.1;

	private MURadar() {
	}

}
