package com.albom.iion.isr.processing;

public class CoherentNoiseFinder {

	private static final int WINDOW_WIDTH_DEFAULT = 15;
	private static final float LEVEL_DEFAULT = 5.0f;
	private static final boolean DIRECTION_DEFAULT = true;

	private int windowWidth;
	private float level;
	private boolean direction;

	public CoherentNoiseFinder() {
		this(CoherentNoiseFinder.WINDOW_WIDTH_DEFAULT, CoherentNoiseFinder.LEVEL_DEFAULT,
				CoherentNoiseFinder.DIRECTION_DEFAULT);
	}

	public CoherentNoiseFinder(int windowWidth, float level, boolean direction) {
		this.windowWidth = windowWidth;
		this.level = level;
		this.direction = direction;
	}

	public boolean[] find(float[] data, boolean[] labels) {
		if (direction)
			return findForward(data, labels);
		else
			return findBack(data);
	}

	public boolean[] find(float[] data) {
		if (direction)
			return findForward(data);
		else
			return findBack(data);
	}

	private boolean[] findForward(float[] data) {
		boolean[] labels = new boolean[data.length];
		return findForward(data, labels);
	}

	private boolean[] findForward(float[] data, boolean[] labels) {
		for (int t = 0; t < data.length - windowWidth - 1; t++) {

			int num = 0;
			double mean = 0;
			for (int offset = 0; offset < windowWidth; offset++) {
				if (!labels[t + offset]) {
					mean += data[t + offset];
					num++;
				}
			}

			if (num > 9) { // TODO change 9 to something else
				mean /= num;

				double dev = 0;
				for (int offset = 0; offset < windowWidth; offset++) {
					if (!labels[t + offset]) {
						dev += Math.pow(data[t + offset] - mean, 2);
					}
				}
				dev /= Math.sqrt(dev / (num - 1));

				if (Math.abs(data[t + windowWidth] - mean) > level * dev) {
					labels[t + windowWidth] = true;
				}

			}
		}
		return labels;
	}

	private boolean[] findBack(float[] data) {
		boolean[] labels = new boolean[data.length];
		return findBack(data, labels);
	}

	private boolean[] findBack(float[] data, boolean labels[]) {
		for (int t = data.length - windowWidth - 1; t > 0; t--) {
			int num = 0;
			double mean = 0;
			for (int offset = 0; offset < windowWidth; offset++) {
				if (!labels[t + offset]) {
					mean += data[t + offset];
					num++;
				}
			}

			if (num > 9) { // TODO change 9 to something else
				mean /= num;

				double dev = 0;
				for (int offset = 0; offset < windowWidth; offset++) {
					if (!labels[t + offset]) {
						dev += Math.pow(data[t + offset] - mean, 2);
					}
				}
				dev /= Math.sqrt(dev / (num - 1));

				if (Math.abs(data[t - 1] - mean) > level * dev) {
					labels[t - 1] = true;
				}

			}

		}
		return labels;
	}

}
