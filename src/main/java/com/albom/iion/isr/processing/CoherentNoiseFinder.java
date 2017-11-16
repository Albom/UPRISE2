package com.albom.iion.isr.processing;

import java.util.ArrayList;

import com.albom.iion.isr.data.Point;

public class CoherentNoiseFinder {

	private static final int WINDOW_WIDTH_DEFAULT = 15;
	private static final double LEVEL_DEFAULT = 5.0;

	private int windowWidth;
	private double level;

	public CoherentNoiseFinder() {
		this(CoherentNoiseFinder.WINDOW_WIDTH_DEFAULT, CoherentNoiseFinder.LEVEL_DEFAULT);
	}

	public CoherentNoiseFinder(int windowWidth, double level) {
		this.windowWidth = windowWidth;
		this.level = level;
	}

	public ArrayList<Boolean> find(ArrayList<Point> data) {
		int length = data.size();
		ArrayList<Boolean> labels = new ArrayList<>(length);
		for (int i = 0; i < length; i++) {
			labels.add(false);
		}
		findForward(data, labels);
		findBack(data, labels);
		return labels;
	}

	public void find(ArrayList<Point> data, ArrayList<Boolean> labels) {
		findForward(data, labels);
		findBack(data, labels);
	}

	public void find(ArrayList<Point> data, ArrayList<Boolean> labels, boolean forward) {
		if (forward) {
			findForward(data, labels);
		} else {
			findBack(data, labels);
		}
	}

	private void findForward(ArrayList<Point> data, ArrayList<Boolean> labels) {
		int length = data.size();
		for (int t = 0; t < length - windowWidth - 1; t++) {

			int num = 0;
			double mean = 0;
			for (int offset = 0; offset < windowWidth; offset++) {
				if (!labels.get(t + offset)) {
					mean += data.get(t + offset).getValue();
					num++;
				}
			}

			if (num > windowWidth / 2) {
				mean /= num;

				double dev = 0;
				for (int offset = 0; offset < windowWidth; offset++) {
					if (!labels.get(t + offset)) {
						dev += Math.pow(data.get(t + offset).getValue() - mean, 2);
					}
				}
				dev = Math.sqrt(dev / (num - 1));

				if (Math.abs(data.get(t + windowWidth).getValue() - mean) > level * dev) {
					labels.set(t + windowWidth, true);
				}

			}
		}
	}

	private void findBack(ArrayList<Point> data, ArrayList<Boolean> labels) {
		int length = data.size();
		for (int t = length - windowWidth - 1; t > 0; t--) {
			int num = 0;
			double mean = 0;
			for (int offset = 0; offset < windowWidth; offset++) {
				if (!labels.get(t + offset)) {
					mean += data.get(t + offset).getValue();
					num++;
				}
			}

			if (num > windowWidth / 2) {
				mean /= num;

				double dev = 0;
				for (int offset = 0; offset < windowWidth; offset++) {
					if (!labels.get(t + offset)) {
						dev += Math.pow(data.get(t + offset).getValue() - mean, 2);
					}
				}
				dev = Math.sqrt(dev / (num - 1));

				if (Math.abs(data.get(t - 1).getValue() - mean) > level * dev) {
					labels.set(t - 1, true);
				}

			}

		}
	}

}
