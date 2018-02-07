package com.albom.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public abstract class StringLogger {

	public static void log(String fileName, String data) {

		Path file = Paths.get(fileName);
		try (BufferedWriter out = Files.newBufferedWriter(file, Charset.forName("US-ASCII"),
				StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
			out.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}