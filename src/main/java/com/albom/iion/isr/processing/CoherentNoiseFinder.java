package com.albom.iion.isr.processing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.albom.iion.isr.data.Point;

/**
 * This class provides methods to find coherent noises for different variations of input data.
 */
public class CoherentNoiseFinder {

	/**
	 * Default value of window width.
	 */
	private static final int WINDOW_WIDTH_DEFAULT = 15;

	/**
	 * Default value of level.
	 */
	private static final double LEVEL_DEFAULT = 5.0;

	private int windowWidth;
	private double level;

	/**
	 * Constructs an object with default values of window width and level.
	 */
	public CoherentNoiseFinder() {
		this(WINDOW_WIDTH_DEFAULT, LEVEL_DEFAULT);
	}

	/**
	 * Constructs an object with specified values of window width and level.
	 *
	 * @param windowWidth
	 * @param level
	 */
	public CoherentNoiseFinder(int windowWidth, double level) {
		this.windowWidth = windowWidth;
		this.level = level;
	}

	/**
	 * Finds coherent noise for input data using forward and back algorithms.
	 *
	 * @param data Input data.
	 * @return Returns collection of labels.
	 */
	public List<Boolean> find(List<Point> data) {
		List<Boolean> labels = new ArrayList<Boolean>(Collections.nCopies(data.size(), false));
		Collections.fill(labels, false);
		find(data, labels);

		return labels;
	}

	/**
	 * Finds coherent noise for input data using forward and back algorithms.
	 * Result is stored into collection from method parameters.
	 *
	 * @param data Input data.
	 * @param labels Collection that contains the result.
	 */
	public void find(List<Point> data, List<Boolean> labels) {
		findForward(data, labels);
		findBack(data, labels);
	}

	/**
	 * Finds coherent noise for input data using either forward (if 'forward' flag is true) or back algorithm (if
	 * 'forward' flag is false).
	 *
	 * @param data
	 * @param labels
	 * @param forward
	 */
	public void find(List<Point> data, List<Boolean> labels, boolean forward) {
		if (forward) {
			findForward(data, labels);
		} else {
			findBack(data, labels);
		}
	}

	private void findForward(List<Point> data, List<Boolean> labels) {
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

	private void findBack(List<Point> data, List<Boolean> labels) {
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
