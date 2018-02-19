package com.albom.iion.isr.processing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.albom.iion.isr.data.Point;

public class HeightIntegratorNum {

	private int num = 0;

	public HeightIntegratorNum(int num) {
		this.num = num;
	}

	public ArrayList<Point> integrate(List<Point> data) {
		int length = data.size();
		ArrayList<Point> result = new ArrayList<>();
		if (length == 0) {
			return result;
		}
		LocalDateTime date = data.get(0).getDate();
		int lag = data.get(0).getLag();
		for (int h = 0; h < length; h++) {
			double value = 0;
			int c = 0;
			for (int i = -num / 2; i <= num / 2; i++) {
				if ((h + i > 0) && (h + i < length)) {
					value += data.get(h + i).getValue();
					c++;
				}
			}
			value /= c;
			result.add(new Point(date, h, lag, value));
		}
		return result;
	}

}
