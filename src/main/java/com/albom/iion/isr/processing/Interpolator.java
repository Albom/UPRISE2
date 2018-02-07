package com.albom.iion.isr.processing;

import java.util.ArrayList;
import java.util.List;

public class Interpolator {

	private static final int WINDOW_WIDTH_DEFAULT = 15;
	private int windowWidth;

	public Interpolator() {
		this(Interpolator.WINDOW_WIDTH_DEFAULT);
	}

	public Interpolator(int windowWidth) {
		this.windowWidth = windowWidth;
	}

	public void replace(List<Double> data, List<Boolean> labels) {
		int length = data.size();
		for (int i = 0; i < length; i++) {
			if (labels.get(i)) {
				double mean = 0;
				double dev = 0;
				int c = 0;
			}
		}
	}

}
