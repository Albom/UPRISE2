package com.albom.iion.isr.processing;

import com.albom.iion.isr.radars.KharkivRadar;
import com.albom.physics.Constants;
import com.albom.physics.IonMass;

public class DirectProblem {

	private double waveLength;
	private double m1, m2, m3;
	private double m2m1;
	private double m3m1;
	private double sqrt_m2m1;
	private double sqrt_m3m1;
	private double sqrt_m2m1_1000;
	private double sqrt_m3m1_1000;
	private double memi;
	private double sqrt_memi;

	private static final double[] PHI = calcPhi();

	public DirectProblem(double waveLength, double m1, double m2, double m3) {
		this.waveLength = waveLength;
		this.m1 = m1;
		this.m2 = m2;
		this.m3 = m3;
		m2m1 = this.m2 / m1;
		m3m1 = this.m3 / m1;
		sqrt_m2m1 = Math.sqrt(m2m1);
		sqrt_m3m1 = Math.sqrt(m3m1);
		sqrt_m2m1_1000 = sqrt_m2m1 * 1000;
		sqrt_m3m1_1000 = sqrt_m3m1 * 1000;
		memi = Constants.MASS_OF_ELECTRON / (Constants.ATOMIC_CONSTANT * m1);
		sqrt_memi = Math.sqrt(memi);
	}

	public DirectProblem(double waveLength) {
		this(waveLength, IonMass.HYDROGEN, IonMass.HELIUM, IonMass.OXYGEN);
	}

	public DirectProblem() {
		this(KharkivRadar.WAVE_LENGTH, IonMass.HYDROGEN, IonMass.HELIUM, IonMass.OXYGEN);
	}

	public DirectProblem(double m1, double m2, double m3) {
		this.waveLength = KharkivRadar.WAVE_LENGTH;
		this.m1 = m1;
		this.m2 = m2;
		this.m3 = m3;
	}

	private static double[] calcPhi() {

		int length = 20000;
		double step = 1e-3;

		double[] phi = new double[length];

		double step2 = step * step;

		phi[0] = 0;

		for (int t = 1; t < length; t++)
			phi[t] = phi[t - 1] + Math.exp(t * t * step2);

		for (int t = 0; t < length; t++)
			phi[t] *= 2 * t * step2 * Math.exp(-t * t * step2);

		for (int t = 0; t < length; t++)
			phi[t] -= t * step2;

		return phi;
	}

	public double spectrum(double g1, double g2, double ti, double te, double ne, boolean isDeby, double freq) {

		double g3 = 1 - g1 - g2;

		double alpha = waveLength / (4 * Math.PI)
				* Math.sqrt(Constants.ATOMIC_CONSTANT * m1 / (2 * Constants.BOLTZMANN * ti));

		if (freq < 0.01) {
			freq = 0.01;
		}

		double theta = 2 * Math.PI * alpha * freq;

		if (theta > 19.999) {
			theta = 19.999;
		}

		double theta2 = theta * theta;
		double theta_sqrt_pi = theta * Math.sqrt(Math.PI);
		double t = te / ti;

		int theta_int1 = (int) (theta * 1000);
		int theta_int2 = theta * sqrt_m2m1 > 19.999 ? 19999 : (int) (theta * sqrt_m2m1_1000);
		int theta_int3 = theta * sqrt_m3m1 > 19.999 ? 19999 : (int) (theta * sqrt_m3m1_1000);

		double realYelect = theta_sqrt_pi * sqrt_memi * Math.pow(t, -0.5) * Math.exp(-memi * theta2 / t);
		double modYelect2 = (1 + realYelect * realYelect);

		double sumRealYion = g1 * theta_sqrt_pi * 1 * Math.exp(-1 * theta2)
				+ g2 * theta_sqrt_pi * sqrt_m2m1 * Math.exp(-m2m1 * theta2)
				+ g3 * theta_sqrt_pi * sqrt_m3m1 * Math.exp(-m3m1 * theta2);
		double sumImagYion = g1 * (1 - PHI[theta_int1]) + g2 * (1 - PHI[theta_int2]) + g3 * (1 - PHI[theta_int3]);

		double tSumRealYion = t * sumRealYion;
		double tSumImagYion = t * sumImagYion;

		double coeffDeby = (4 * Math.PI * waveLength) * (4 * Math.PI * waveLength) * Constants.EPSILON_0
				* Constants.BOLTZMANN / (Constants.CHARGE_OF_ELECTRON * Constants.CHARGE_OF_ELECTRON);
		double deby = isDeby ? (coeffDeby * te / (ne > 1 ? ne : 1)) : 0;

		return (modYelect2 * sumRealYion
				+ (tSumRealYion * tSumRealYion + (tSumImagYion + deby) * (tSumImagYion + deby)) * realYelect)
				/ (((realYelect + tSumRealYion) * (realYelect + tSumRealYion)
						+ (1 + tSumImagYion + deby) * (1 + tSumImagYion + deby)) * theta);

	}

	public double spectrum(double ti, double te, double freq) {
		return spectrum(0, 0, ti, te, 1, false, freq);
	}

	public double spectrum(double g1, double g2, double ti, double te, double freq) {
		return spectrum(g1, g2, ti, te, 1, false, freq);
	}

	public double[] spectrum(double g1, double g2, double ti, double te, double ne, boolean isDeby, double freq[]) {
		double[] result = new double[freq.length];
		for (int f = 0; f < freq.length; f++) {
			result[f] = spectrum(g1, g2, ti, te, ne, isDeby, f);
		}
		return result;
	}

	public double[] spectrum(double ti, double te, double freq[]) {
		double[] result = new double[freq.length];
		for (int f = 0; f < freq.length; f++) {
			result[f] = spectrum(0, 0, ti, te, 1, false, f);
		}
		return result;
	}
	
	public double[] spectrum(double g1, double g2, double ti, double te, double freq[]) {
		double[] result = new double[freq.length];
		for (int f = 0; f < freq.length; f++) {
			result[f] = spectrum(g1, g2, ti, te, 1, false, f);
		}
		return result;
	}


	
}
