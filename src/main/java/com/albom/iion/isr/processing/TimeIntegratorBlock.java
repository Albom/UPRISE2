package com.albom.iion.isr.processing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.albom.iion.isr.data.Point;
import com.albom.utils.Time;

public class TimeIntegratorBlock {

	private int block;

	/**
	 * 
	 * @param block
	 *            max block length in seconds
	 */
	public TimeIntegratorBlock(int block) {
		this.block = block;
	}

	public List<Point> integrate(List<Point> data) {

		int length = data.size();

		List<Point> result = new ArrayList<>();
		if (length == 0) {
			return result;
		}

		int cur = 0;
		Point startPoint = data.get(cur);
		int lag = startPoint.getLag();
		int alt = startPoint.getAlt();
		LocalDateTime startTime = startPoint.getDate();
		double value = startPoint.getValue();
		int c = 1;
		while (cur < length - 1) {
			Point currentPoint = data.get(cur);
			Point nextPoint = data.get(cur + 1);
			LocalDateTime currentTime = currentPoint.getDate();
			LocalDateTime nextTime = nextPoint.getDate();
			if (Time.diff(currentTime, nextTime) <= block) {
				value += nextPoint.getValue();
				c++;
			} else {
				LocalDateTime time = startTime.plusSeconds(Time.diff(startTime, currentTime) / 2);
				result.add(new Point(time, alt, lag, value / c));
				value = currentPoint.getValue();
				c = 1;
				startTime = data.get(cur).getDate();
			}
			cur++;
		}

		return result;
	}

}
