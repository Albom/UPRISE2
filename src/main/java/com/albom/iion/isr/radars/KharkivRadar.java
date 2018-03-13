package com.albom.iion.isr.radars;

import com.albom.physics.Constants;

public final class KharkivRadar {

	public final static double FREQUENCY = 158.0036e6;
	public final static double WAVE_LENGTH = Constants.SPEED_OF_LIGHT / FREQUENCY;

	public final static double LATITUDE = 49.676;
	public final static double LONGITUDE = 36.292;
	
	public final static double INVARIANT_LATITUDE = 45.74;
	
	private KharkivRadar() {
	}

}
