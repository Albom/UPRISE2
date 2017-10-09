package com.albom.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

public abstract class Time {

	public static double toDecimal(String h, String m, String s) {
		return toDecimal(Integer.valueOf(h), Integer.valueOf(m), Integer.valueOf(s));
	}

	public static double toDecimal(LocalDateTime date) {
		int h = date.getHour();
		int m = date.getMinute();
		int s = date.getSecond();
		return toDecimal(h, m, s);
	}
	
	public static double toDecimal(int h, int m, int s) {
		return (double) (h) + (double) (m) / 60.0 + (double) (s) / 3600.0;
	}

	public static double reduce(double hour) {
		return hour - 24 * (((int) hour) / 24);
	}

	public static double hours(double hour) {
		return 24 * (((int) hour) / (int) 24);
	}

	public static long[] diff(LocalDateTime[] times) {

		if ((times == null) || (times.length < 2))
			return null;

		long[] result = new long[times.length - 1];
		ZoneId zoneId = ZoneId.systemDefault();
		for (int i = 0; i < times.length - 1; i++) {
			result[i] = times[i + 1].atZone(zoneId).toEpochSecond() - times[i].atZone(zoneId).toEpochSecond();
		}
		return result;

	}

	public static long diff(LocalDateTime start, LocalDateTime end) {
		ZoneId zoneId = ZoneId.systemDefault();
		return end.atZone(zoneId).toEpochSecond() - start.atZone(zoneId).toEpochSecond();
	}

}
