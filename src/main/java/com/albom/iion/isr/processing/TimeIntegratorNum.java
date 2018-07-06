package com.albom.iion.isr.processing;

import java.util.ArrayList;
import java.util.List;

import com.albom.iion.isr.data.Point;
import com.albom.util.Time;

public class TimeIntegratorNum {

	private int num;
	private int step;

	public TimeIntegratorNum(int num, int step) {
		this.num = num;
		this.step = step;
	}

	public TimeIntegratorNum(int num) {
		this(num, 1);
	}

	public List<Point> integrate(List<Point> data) {

		int length = data.size();
		List<Point> result = new ArrayList<>();
		if (length == 0) {
			return result;
		}

		int lag = data.get(0).getLag();
		int alt = data.get(0).getAlt();

		for (int t = 0; t < length - num; t += step) {
			double value = 0;
			for (int offset = 0; offset < num; offset++) {
				value += data.get(t + offset).getValue();
			}
			result.add(new Point(
					data.get(t).getDate()
							.plusSeconds(Time.diff(data.get(t).getDate(), data.get(t + num - 1).getDate()) / 2),
					alt, lag, value / num));
		}

		return result;

	}

}
