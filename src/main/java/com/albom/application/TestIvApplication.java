package com.albom.application;

import java.nio.file.Paths;
import java.util.List;

import com.albom.iion.isr.data.kharkiv.IvFile;
import com.albom.iion.isr.data.kharkiv.IvFileFS;
import com.albom.utils.Directory;

public class TestIvApplication {

	private void run() {

		String directory = "e:/Data/26-12-2017/1/";
		
		List<String> fileNames = Directory.listFileNames(directory);

		for (String fileName : fileNames) {
			IvFile iv = IvFileFS.load(Paths.get(directory + fileName));
			System.out.println(iv);
		}
	}

	public static void main(String[] args) {

		TestIvApplication app = new TestIvApplication();
		app.run();

	}

}
