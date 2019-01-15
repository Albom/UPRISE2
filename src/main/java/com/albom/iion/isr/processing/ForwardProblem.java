package com.albom.iion.isr.processing;

import com.albom.iion.isr.radars.KharkivRadar;
import com.albom.iion.isr.radars.MillstoneHillRadar;
import com.albom.physics.Constants;
import com.albom.physics.IonMass;

public class ForwardProblem {

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

	// parameters for precalculated cosine table
	private double deltaF = 0;
	private double deltaTau = 0;
	private int acfLength = 0;
	private int nHarm = 0;
	private double[][] cos = null;

	private static final double[] PHI = calcPhi();

	private int defaultNumberOfHarmonics;
	private double defaultDeltaF;

	public ForwardProblem(double waveLength, double m1, double m2, double m3) {
		this.waveLength = waveLength;
		defaultNumberOfHarmonics = estimateNumberOfHarmonics();
		defaultDeltaF = estimateDeltaF();
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

	public ForwardProblem(double waveLength) {
		this(waveLength, IonMass.HYDROGEN, IonMass.HELIUM, IonMass.OXYGEN);
	}

	public ForwardProblem() {
		this(KharkivRadar.WAVE_LENGTH, IonMass.HYDROGEN, IonMass.HELIUM, IonMass.OXYGEN);
	}

	public ForwardProblem(double m1, double m2, double m3) {
		this(KharkivRadar.WAVE_LENGTH, m1, m2, m3);
		/*
		this.waveLength = KharkivRadar.WAVE_LENGTH;
		this.m1 = m1;
		this.m2 = m2;
		this.m3 = m3;*/
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

	public void acf(double g1, double g2, double ti, double te, double ne, boolean isDeby, int nHarm, double deltaF,
			double deltaTau, double acf[]) {
		double[] spectrum = new double[nHarm];

		if ((Math.abs(this.deltaTau - deltaTau) > 1.0e-8) || (this.acfLength != acf.length) || (this.nHarm != nHarm)
				|| (this.deltaF != deltaF)) {

			this.acfLength = acf.length;
			this.deltaTau = deltaTau;
			this.deltaF = deltaF;
			this.nHarm = nHarm;

			cos = new double[this.acfLength][nHarm];

			for (int j = 0; j < this.acfLength; j++) {
				for (int i = 0; i < nHarm; i++) {
					cos[j][i] = Math.cos(2 * Math.PI * deltaTau * deltaF * j * i);
				}
			}

		}

		for (int f = 1; f < nHarm; f++) {
			spectrum[f] = spectrum(g1, g2, ti, te, ne, isDeby, f * deltaF);
		}
		spectrum[0] = spectrum[1];

		for (int j = 0; j < acf.length; j++) {
			acf[j] = 0;
			for (int i = 0; i < nHarm - 2; i += 2) {
				acf[j] += (spectrum[i] * cos[j][i] + 4 * spectrum[i + 1] * cos[j][i + 1]
						+ spectrum[i + 2] * cos[j][i + 2]);
			}
		}

		for (int j = acf.length - 1; j > -1; j--) {
			acf[j] /= acf[0];
		}

	}

	public void acf(double g1, double g2, double ti, double te, double ne, boolean isDeby, double deltaTau,
			double acf[]) {
		acf(g1, g2, ti, te, ne, isDeby, defaultNumberOfHarmonics, defaultDeltaF, deltaTau, acf);
	}

	public void acf(double g1, double g2, double ti, double te, double deltaTau, double acf[]) {
		acf(g1, g2, ti, te, 1, false, defaultNumberOfHarmonics, defaultDeltaF, deltaTau, acf);
	}

	public void acf(double ti, double te, double deltaTau, double acf[]) {
		acf(0, 0, ti, te, 1, false, defaultNumberOfHarmonics, defaultDeltaF, deltaTau, acf);
	}

	public double[] acf(double g1, double g2, double ti, double te, double deltaTau, int length) {
		double[] acf = new double[length];
		acf(g1, g2, ti, te, 1, false, defaultNumberOfHarmonics, defaultDeltaF, deltaTau, acf);
		return acf;
	}

	public double[] acf(double ti, double te, double deltaTau, int length) {
		double[] acf = new double[length];
		acf(0, 0, ti, te, 1, false, defaultNumberOfHarmonics, defaultDeltaF, deltaTau, acf);
		return acf;
	}

	private int estimateNumberOfHarmonics() {

		if (Math.abs(waveLength - KharkivRadar.WAVE_LENGTH) < 0.1) {
			return 5000;
		} else if (Math.abs(waveLength - MillstoneHillRadar.WAVE_LENGTH) < 0.1) {
			return 600;
		} else {
			return 800;
		}
	}

	private double estimateDeltaF() {

		if (Math.abs(waveLength - KharkivRadar.WAVE_LENGTH) < 0.1) {
			return 3;
		} else if (Math.abs(waveLength - MillstoneHillRadar.WAVE_LENGTH) < 0.1) {
			return 100;
		} else {
			return 50;
		}
	}

}
