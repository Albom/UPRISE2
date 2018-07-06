package com.albom.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public abstract class Time {
	
	public final static DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

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

	public static void linear(double[] times) {
		for (int t = 1; t < times.length; t++) {
			while (times[t] < times[t - 1]) {
				times[t] += 24.0;
			}
		}
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

	public static LocalDateTime getStartHour(LocalDateTime oldTime) {
		LocalDateTime newTime = LocalDateTime.from(oldTime);
		newTime = newTime.truncatedTo(ChronoUnit.HOURS);
		return newTime;
	}

	public static LocalDateTime getEndHour(LocalDateTime oldTime) {
		return getStartHour(oldTime).plusHours(1);
	}

}
