package com.albom.utils;

public abstract class Time {

	public static double toDecimal(String h, String m, String s)
	{
		return Double.valueOf(h) + Double.valueOf(m)/60.0 + Double.valueOf(s)/3600.0;
	}
	
	public static double toDecimal(int h, int m, int s)
	{
		return (double)(h) + (double)(m)/60.0 + (double)(s)/3600.0;
	}

	public static double reduce(double hour){
		return hour - 24*(((int)hour)/24);
	}
	
	public static double hours(double hour){
		return 24*(((int)hour)/(int)24);
	}
	
}
