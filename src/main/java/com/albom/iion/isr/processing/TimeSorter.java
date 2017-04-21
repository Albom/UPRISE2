package com.albom.iion.isr.processing;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class TimeSorter {

	private final static int INTERVAL = 60;
	private final static int DELTA = 4;
	private int interval;

	public TimeSorter() {
		this(INTERVAL + DELTA);
	}

	public TimeSorter(int interval) {
		this.interval = interval;
	}

	public LocalDateTime[] sort(LocalDateTime[] in) {
		if ((in == null) || (in.length == 0)) {
			return null;
		}
		ArrayList<LocalDateTime> dates = new ArrayList<LocalDateTime>(Arrays.asList(in));
		Collections.sort(dates);
		LinkedList<LocalDateTime> out = new LinkedList<LocalDateTime>();
		out.add(dates.get(0));

		ZoneId zoneId = ZoneId.systemDefault();

		System.out.println(" In:" + out.get(0));

		for (int i = 1; i < in.length; i++) {
			long diff = dates.get(i).atZone(zoneId).toEpochSecond() - dates.get(i - 1).atZone(zoneId).toEpochSecond();
			if (diff > 2 * interval) {
				for (int j = 0; j < diff / interval ; j++) {
					out.add(out.peekLast().plusSeconds(diff / (diff / interval + 1)));
				}
			}
			out.add(dates.get(i));
		}

		LocalDateTime[] result = new LocalDateTime[out.size()];
		for (int i = 0; i < out.size(); i++) {
			result[i] = out.get(i);
		}

		return result;
	}

}
