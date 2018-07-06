package com.albom.iion.isr.processing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.albom.iion.isr.data.Point;
import com.albom.util.Time;

public class TimeIntegratorPoints {

	private int points;

	public TimeIntegratorPoints(int points) {
		this.points = points;
	}

	public List<Point> integrate(List<Point> data, List<Boolean> labels) {
		int length = data.size();
		List<Point> result = new ArrayList<>();

		if (length == 0) {
			return result;
		}

		Point p = data.get(0);
		int lag = p.getLag();
		int alt = p.getAlt();

		for (int t = 0; t < length; t++) {
			double value = 0;
			int c = 0;
			int offset = 0;
			LocalDateTime start = data.get(t).getDate();
			while ((t + offset < points) && (t + offset < length)) {
				if (!labels.get(t + offset)) {
					value += data.get(t + offset).getValue();
					c++;
				}
				offset++;
			}
			if (c > 0) {
				result.add(new Point(start.plusSeconds(Time.diff(start, data.get(t + c).getDate()) / 2), alt, lag,
						value / c));
			}
		}

		return result;
	}

}
