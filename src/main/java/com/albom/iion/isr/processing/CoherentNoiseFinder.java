package com.albom.iion.isr.processing;

public class CoherentNoiseFinder {

	private static final int WINDOW_WIDTH_DEFAULT = 15;
	private static final float LEVEL_DEFAULT = 5.0f;
	private static final boolean DIRECTION_DEFAULT = true;

	private int windowWidth;
	private float level;
	private boolean direction;

	CoherentNoiseFinder() {
		this(CoherentNoiseFinder.WINDOW_WIDTH_DEFAULT, CoherentNoiseFinder.LEVEL_DEFAULT,
				CoherentNoiseFinder.DIRECTION_DEFAULT);
	}

	CoherentNoiseFinder(int windowWidth, float level, boolean direction) {
		this.windowWidth = windowWidth;
		this.level = level;
		this.direction = direction;
	}
	
	boolean[] find(float[] data){
		boolean[] labels = new boolean[data.length];
		return labels;
	}

}
