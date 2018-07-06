package com.albom.iion.isr.data;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.albom.utils.Time;

public class Point {
	private LocalDateTime date;
	private int alt;
	private int lag;

	// TODO replace double to Complex from Apache Commons
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
	 * @param lag
	 *            the lag to set
	 */
	public void setLag(int lag) {
		this.lag = lag;
	}

	public static List<Double> getValues(List<Point> points) {
		return points.stream().map(Point::getValue).collect(Collectors.toList());
	}

	public static List<LocalDateTime> getDates(List<Point> points) {
		return points.stream().map(Point::getDate).collect(Collectors.toList());
	}

	public static void setValues(List<Point> points, List<Double> values) {
		int length = points.size();
		for (int i = 0; i < length; i++) {
			points.get(i).setValue(values.get(i));
		}
	}

	public static void log(Path file, List<Point> points) {
		log(file, points, null);
	}

	public static void log(Path file, List<Point> points, List<Boolean> labels) {

		try (BufferedWriter out = Files.newBufferedWriter(file, Charset.forName("US-ASCII"))) {

			int length = points.size();

			for (int i = 0; i < length; i++) {
				out.write(String.format(Locale.US, "%c %d %s %d %d %9.6e %d\n",
						(labels != null ? labels.get(i) : false) ? '#' : ' ', i,
						points.get(i).getDate().format(Time.DATE_TIME_FORMAT), points.get(i).getAlt(),
						points.get(i).getLag(), points.get(i).getValue(),
						(labels != null ? labels.get(i) : false) ? 1 : 0));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
