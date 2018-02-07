package com.albom.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class DoubleTableLogger {

	public static void log(String fileName, double[][] data) {

		Path file = Paths.get(fileName);
		try (BufferedWriter out = Files.newBufferedWriter(file, Charset.forName("US-ASCII"))) {
			for (int i = 0; i < data.length; i++){
				for (int j = 0; j < data[i].length; j++){
					out.write(data[i][j] + "\t");
				}
				out.write("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	
}
