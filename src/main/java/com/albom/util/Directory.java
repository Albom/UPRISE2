package com.albom.util;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Directory {

	public static List<String> list(String directory) {
		List<String> fileNames = new ArrayList<>();
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {
			for (Path path : directoryStream) {
				fileNames.add(path.toString());
			}
		} catch (IOException ex) {
		}
		return fileNames;
	}

	public static List<String> listFileNames(String directory) {
		List<String> files = list(directory);
		List<String> result = new ArrayList<>();
		for (String f : files) {
			Path p = Paths.get(f);
			if (!Files.isDirectory(p)) {
				result.add(p.getFileName().toString());
			}
		}
		return result;
	}

	
	public static List<String> listRecursively(String directory) {
		List<String> files = list(directory);
		List<String> result = new ArrayList<>();
		for (String f : files) {
			Path p = Paths.get(f);
			if (!Files.isDirectory(p)) {
				result.add(p.toString());
			} 
			else result.addAll(listRecursively(f));
		}
		return result;
	}
	
}
