package com.albom.iion.isr.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Point {
	private LocalDateTime date;
	private int alt;
	private int lag;
	private double value;

	public Point(LocalDateTime date, int alt, int lag, double value) {
		this.date = date;
		this.alt = alt;
		this.lag = lag;
		this.value = value;
	}

	/**
	 * @return the date
	 */
	public LocalDateTime getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	/**
	 * @return the alt
	 */
	public int getAlt() {
		return alt;
	}

	/**
	 * @param alt
	 *            the alt to set
	 */
	public void setAlt(int alt) {
		this.alt = alt;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "[" + date + ", alt=" + alt + ", lag=" + lag + ": " + value + "]";
	}

	/**
	 * @return the lag
	 */
	public int getLag() {
		return lag;
	}

	/**
	 * @param lag the lag to set
	 */
	public void setLag(int lag) {
		this.lag = lag;
	}

	public static List<Double> getValues(ArrayList<Point> points){
		return points.stream()
				.map(Point::getValue)
				.collect(Collectors.toList());
	}

	public static List<LocalDateTime> getDates(List<Point> points){
		return points.stream()
				.map(Point::getDate)
				.collect(Collectors.toList());
	}
	
	public static void setValues(List<Point> points, List<Double> values){
		int length = points.size();
		for (int i = 0; i < length; i++) {
			points.get(i).setValue(values.get(i));
		}
	}
	
}
