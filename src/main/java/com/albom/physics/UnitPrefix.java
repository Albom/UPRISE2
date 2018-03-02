package com.albom.physics;

public abstract class UnitPrefix {

	public static double micro(double x) {
		return x * 1e-6;
	}

	public static double milli(double x) {
		return x * 1e-3;
	}

	public static double mega(double x) {
		return x * 1e6;
	}

}
