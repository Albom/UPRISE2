package com.albom.iion.isr.processing;

import java.util.Arrays;

import com.albom.iion.isr.radars.KharkivRadar;

public class InverseProblem {

	ForwardProblem forward;
	double deltaTau;

	public InverseProblem(ForwardProblem forward, double deltaTau) {
		this.forward = forward;
		this.deltaTau = deltaTau;
	}

	private double function(double ti, double te, double[] acfExp) {

		double[] acfTheor = new double[acfExp.length];

		forward.acf(ti, te, deltaTau, acfTheor);

		double delta = 0;
		for (int tau = 0; tau < acfExp.length; tau++) {
			delta += (acfExp[tau] - acfTheor[tau]) * (acfExp[tau] - acfTheor[tau]);
		}

		return delta;
	}

	public double[] find(double[] acfExp) {
		double delta = 1;
		double delta2 = delta * delta;
		double err = 1;
		double min = Double.MAX_VALUE;
		double ti = 600;
		double te = 600;

		int counter = 0;
		while (min > err && counter < 150) {

			double fTiTe = function(ti, te, acfExp);
			double fTipTe = function(ti + delta, te, acfExp);
			double fTiTep = function(ti, te + delta, acfExp);
			double fTipTep = function(ti + delta, te + delta, acfExp);
			double fTimTe = function(ti - delta, te, acfExp);
			double fTiTem = function(ti, te - delta, acfExp);

			double[][] hessian = new double[2][2];
			hessian[0][0] = (fTipTep - 2 * fTiTe + fTimTe) / delta2;
			hessian[0][1] = (fTipTep - fTipTe - fTiTep + fTiTe) / delta2;
			hessian[1][0] = hessian[0][1];
			hessian[1][1] = (fTiTep - 2 * fTiTe + fTiTem) / delta2;

			double det = 1.0 / (hessian[0][0] * hessian[1][1] - hessian[0][1] * hessian[1][0]);

			double[][] hessian_1 = new double[2][2];
			hessian_1[0][0] = hessian[1][1] * det;
			hessian_1[0][1] = -hessian[1][0] * det;
			hessian_1[1][0] = hessian_1[0][1];
			hessian_1[1][1] = hessian[0][0] * det;

			double[] jac = new double[2];
			jac[0] = (fTipTe - fTimTe) / (2 * delta);
			jac[1] = (fTiTep - fTiTem) / (2 * delta);

			double[] dot = new double[2];
			dot[0] = hessian_1[0][0] * jac[0] + hessian_1[0][1] * jac[1];
			dot[1] = hessian_1[1][0] * jac[0] + hessian_1[1][1] * jac[1];

			ti -= dot[0];
			te -= dot[1];
			min = Math.hypot(dot[0], dot[1]);
//			System.out.println(dot[0] +"\t"+ dot[1] +"\t"+ti + "\t" + te + "\t" + min);

			counter++;

		}

		return new double[] { ti, te };

	}

	public static void main(String[] arg) {

		final double DELTA_TAU = KharkivRadar.MODE_12_SAMPLING_RATE * 1e-6;
		final int LEN = 19;

		ForwardProblem fp = new ForwardProblem();
		InverseProblem ip = new InverseProblem(fp, DELTA_TAU);

		double[] acfExp = new double[LEN];
		fp.acf(500, 700, DELTA_TAU, acfExp);

		double[] t = ip.find(acfExp);
		System.out.println(Arrays.toString(t));

	}

}
