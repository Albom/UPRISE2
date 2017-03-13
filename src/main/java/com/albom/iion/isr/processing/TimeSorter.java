package com.albom.iion.isr.processing;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class TimeSorter {

	private final static int INTERVAL = 60 + 4;
	private int interval;

	public TimeSorter() {
		this(INTERVAL);
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
		
		for (int i = 1; i < in.length; i++) {
			if (dates.get(i).atZone(zoneId).toEpochSecond() - out.peekLast().atZone(zoneId).toEpochSecond() > 2
					* interval) {
				// out.peekLast() ) dates.get(i) )
			}
		}
		System.out.print(dates.toString());
		return null;// (LocalDateTime[]) dates.toArray();
	}

}
