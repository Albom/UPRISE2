package com.albom.iion.isr.processing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.albom.iion.isr.data.Point;
import com.albom.util.Time;

public class TimeIntegrator {

	private int interval = 0;

	/**
	 * 
	 * @param interval
	 *            integration time in seconds
	 */
	public TimeIntegrator(int interval) {
		this.interval = interval;
	}

	public List<Point> integrate(List<Point> data, List<Boolean> labels) {

		int length = data.size();
		List<Point> result = new ArrayList<>();

		if (length == 0) {
			return result;
		}

		LocalDateTime start = Time.getStartHour(data.get(0).getDate());
		LocalDateTime end = Time.getEndHour(data.get(length - 1).getDate());
		LocalDateTime current = LocalDateTime.from(start);

		int lag = data.get(0).getLag();
		int alt = data.get(0).getAlt();

		while (Time.diff(current, end) > 0) {
			LocalDateTime currentEnd = current.plusSeconds(interval);
			int c = 0;
			double value = 0;
			for (int i = 0; i < length; i++) {
				LocalDateTime time = data.get(i).getDate();
				if ((Time.diff(current, time) >= 0) && (Time.diff(time, currentEnd) > 0) && (!labels.get(i))) {
					value += data.get(i).getValue();
					c++;
				}
			}
			if (c != 0) {
				result.add(new Point(current.plusSeconds(interval / 2), alt, lag, value / c));
			}
			current = currentEnd;
		}

		return result;
	}

}
