package com.albom.iion.isr.data;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Point {
	private LocalDateTime date;
	private int alt;
	private int lag;
	private double value;

	public Point(LocalDateTime date, int alt, int lag, double value) {
		this.setDate(date);
		this.setAlt(alt);
		this.setLag(lag);
		this.setValue(value);
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
		return "[" + date + ", alt=" + alt + ": " + value + "]";
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

	public static ArrayList<Double> getValues(ArrayList<Point> points){
		ArrayList<Double> values = new ArrayList<>(points.size());
		for (Point p : points) {
			values.add(p.getValue());
		}
		return values;
	}

	public static ArrayList<LocalDateTime> getDates(ArrayList<Point> points){
		ArrayList<LocalDateTime> dates = new ArrayList<>(points.size());
		for (Point p : points) {
			dates.add(p.getDate());
		}
		return dates;
	}
	
	public static void setValues(ArrayList<Point> points, ArrayList<Double> values){
		int length = points.size();
		for (int i = 0; i < length; i++) {
			points.get(i).setValue(values.get(i));
		}
	}
	
}
