package com.albom.iion.isr.processing;

import com.albom.iion.isr.radars.KharkivRadar;
import com.albom.physics.IonMass;

public class DirectProblem {

	private double waveLength;
	private double m1, m2, m3;

	private static final double[] PHI = calcPhi();

	public DirectProblem(double waveLength, double m1, double m2, double m3) {
		this.waveLength = waveLength;
		this.m1 = m1;
		this.m2 = m2;
		this.m3 = m3;
	}

	public DirectProblem(double waveLength) {
		this.waveLength = waveLength;
		this.m1 = IonMass.HYDROGEN;
		this.m2 = IonMass.HELIUM;
		this.m3 = IonMass.OXYGEN;
	}

	public DirectProblem() {
		this.waveLength = KharkivRadar.WAVE_LENGTH;
		this.m1 = IonMass.HYDROGEN;
		this.m2 = IonMass.HELIUM;
		this.m3 = IonMass.OXYGEN;
	}

	public DirectProblem(double m1, double m2, double m3) {
		this.waveLength = KharkivRadar.WAVE_LENGTH;
		this.m1 = m1;
		this.m2 = m2;
		this.m3 = m3;
	}

	private static double[] calcPhi() {
		double[] phi = new double[20000];
		return phi;
	}
}
