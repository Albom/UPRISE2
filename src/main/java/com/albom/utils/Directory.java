package com.albom.utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public abstract class Directory {

	public static ArrayList<String> list(String directory) {
		ArrayList<String> fileNames = new ArrayList<>();
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {
			for (Path path : directoryStream) {
				fileNames.add(path.toString());
			}
		} catch (IOException ex) {
		}
		return fileNames;
	}

	public static ArrayList<String> listFileNames(String directory) {
		ArrayList<String> files = list(directory);
		ArrayList<String> result = new ArrayList<>();
		for (String f : files) {
			Path p = Paths.get(f);
			if (!Files.isDirectory(p)) {
				result.add(p.getFileName().toString());
			}
		}
		return result;
	}

	public static ArrayList<String> listFileNamesRecursively(String directory) {
		ArrayList<String> files = list(directory);
		ArrayList<String> result = new ArrayList<>();
		for (String f : files) {
			Path p = Paths.get(f);
			if (!Files.isDirectory(p)) {
				result.add(p.getFileName().toString());
			} 
			else result.addAll(listFileNamesRecursively(f));
		}
		return result;
	}
	
}
