package com.albom.application;

import com.albom.iion.isr.processing.ForwardProblem;
import com.albom.iion.isr.radars.KharkivRadar;
import com.albom.iion.isr.radars.MillstoneHillRadar;
import com.albom.physics.IonMass;

public class TestApplication {

	public static void main(String[] args) {

		ForwardProblem d = new ForwardProblem(MillstoneHillRadar.WAVE_LENGTH, IonMass.OXYGEN, IonMass.HEAVY_MOLECULAR,
				IonMass.HEAVY_MOLECULAR);
//		ForwardProblem d = new ForwardProblem(KharkivRadar.WAVE_LENGTH, IonMass.OXYGEN, IonMass.HEAVY_MOLECULAR,
//				IonMass.HEAVY_MOLECULAR);

		double[] acf = new double[19];
		d.acf(0.11, 0.89, 760, 1000, 15e-6, acf);

		double[] acf2 = new double[19];

		double delta_c = 1e200;
		int ti_c = 500;
		int te_c = 500;

		for (int ti = 200; ti <= 2000; ti += 10) {
			for (int te = ti; te <= 2000; te += 10) {
				d.acf(0.2, 0.8, ti, te, 15e-6, acf2);
//				d.acf(0.11, 0.89, ti, te, 15e-6, acf2);
//				d.acf(0.0, 1.0, ti, te, 15e-6, acf2);

				double delta = 0;
				for (int i = 1; i < 18; i++) {
					delta += (acf[i] - acf2[i]) * (acf[i] - acf2[i]);
				}

				if (delta < delta_c) {
					ti_c = ti;
					te_c = te;
					delta_c = delta;
				}
			}

		}

		System.out.println("out:" + ti_c + " " + te_c);

	}

}
