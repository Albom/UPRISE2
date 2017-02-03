package com.albom.iion.isr.processing;

public class TrapezoidalIntegrator {

	private final static int PARAMETER_DEFAULT = 0;
	private final static boolean DIVIDE_DEFAULT = true;

	private int parameter;
	private boolean divide;
	private int lag;

	public TrapezoidalIntegrator(int lag, int parameter, boolean divide) {
		this.lag = lag;
		this.parameter = parameter;
		this.divide = divide;
	}

	public TrapezoidalIntegrator(int lag) {
		this.lag = lag;
		this.parameter = TrapezoidalIntegrator.PARAMETER_DEFAULT;
		this.divide = TrapezoidalIntegrator.DIVIDE_DEFAULT;
	}

	public float[] integrate(float[] data) {

		float[] result = new float[data.length];

		for (int h = lag; h < data.length - parameter; h++) {
			for (int z = h - lag - parameter; z < h + parameter; z++) {
				result[h] += data[z];
			}

			if (divide) {
				result[h] /= lag + 2 * parameter + 1;
			}
		}

		return result;
	}

}
