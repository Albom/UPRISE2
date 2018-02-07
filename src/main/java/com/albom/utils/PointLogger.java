package com.albom.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Locale;

import com.albom.iion.isr.data.Point;

public abstract class PointLogger {

	public static void log(String fileName, ArrayList<Point> data) {

		log(fileName, data, null);

	}

	public static void log(String fileName, ArrayList<Point> data, ArrayList<Boolean> labels) {

		Path file = Paths.get(fileName);
		try (BufferedWriter out = Files.newBufferedWriter(file, Charset.forName("US-ASCII"))) {

			int length = data.size();
			
			double[] times = new double[length];
			for (int i = 0; i < length; i++){
				times[i] = Time.toDecimal(data.get(i).getDate());
			}
			
			linear(times);
			
			for (int i = 0; i < length; i++) {
				out.write(String.format(Locale.US, "%c %d %9.3f %9.3f %d\n", (labels != null ? labels.get(i) : false) ? '#' : ' ', i, times[i],
						data.get(i).getValue(), (labels != null ? labels.get(i) : false) ? 1 : 0));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void linear(double[] times){
		for (int t = 1; t < times.length; t++){
		    while (times[t] < times[t-1]){
		    	times[t] += 24.0;
		    }
		}	
	}

}
