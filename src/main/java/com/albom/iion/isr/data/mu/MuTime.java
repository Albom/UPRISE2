package com.albom.iion.isr.data.mu;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public abstract class MuTime {

	public static LocalTime toTime(byte[] time) {
		return LocalTime.parse(new String(time), new DateTimeFormatterBuilder().parseCaseInsensitive()
				.appendPattern("HH:mm:ss.SS ").toFormatter(Locale.ENGLISH));
	}
	
	public static String toString(LocalTime time) {
		return time.format(DateTimeFormatter.ofPattern("HH:mm:ss.SS", Locale.ENGLISH));
	}

}
