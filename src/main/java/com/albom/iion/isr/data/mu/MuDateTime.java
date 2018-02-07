package com.albom.iion.isr.data.mu;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public abstract class MuDateTime {

	public static String toString(LocalDateTime date) {
		return date.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SS", Locale.ENGLISH)).toUpperCase();
	}

	public static LocalDateTime toDateTime(byte[] dateTime) {
		return LocalDateTime.parse(new String(dateTime), new DateTimeFormatterBuilder().parseCaseInsensitive()
				.appendPattern("dd-MMM-yyyy HH:mm:ss    ").toFormatter(Locale.ENGLISH));
	}

	public static LocalDateTime toDateTimeWithFraction(byte[] dateTime) {
		return LocalDateTime.parse(new String(dateTime), new DateTimeFormatterBuilder().parseCaseInsensitive()
				.appendPattern("dd-MMM-yyyy HH:mm:ss.SS ").toFormatter(Locale.ENGLISH));
	}
	
}
