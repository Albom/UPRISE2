package com.albom.iion.isr.radars;

import com.albom.physics.Constants;

public final class MillstoneHillRadar {

	public final static double FREQUENCY = 440.2e6;
	public final static double WAVE_LENGTH = Constants.SPEED_OF_LIGHT / FREQUENCY;

	public final static double LATITUDE = 42.61950;
	public final static double LONGITUDE = 288.50827;
	
	public final static double INVARIANT_LATITUDE = 53.40967;
	
	public final static double ALTITUDE = 146; // in meters
	
	private MillstoneHillRadar() {
	}

}
