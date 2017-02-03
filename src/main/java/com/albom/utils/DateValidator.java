package com.albom.utils;


import java.util.Calendar;
import java.util.Date;

public abstract class DateValidator {
	public static boolean validate(int day, int month, int year){
		Calendar c = Calendar.getInstance();
		c.setLenient(false);
		c.set(year, month-1, day);
		try {
		new Date(c.getTimeInMillis());
		} catch (Exception e){
			return false;
		}
		return true;
	}
}