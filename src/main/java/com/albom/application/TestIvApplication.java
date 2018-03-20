package com.albom.application;

import java.nio.file.Paths;
import java.util.List;

import com.albom.iion.isr.data.kharkiv.IvFile;
import com.albom.iion.isr.data.kharkiv.IvFileFS;
import com.albom.iion.isr.data.kharkiv.IvScan;
import com.albom.utils.Directory;

public class TestIvApplication {

	private void test1() {

		String directory = "e:/Data/26-12-2017/1/";

		List<String> fileNames = Directory.listFileNames(directory);

		for (String fileName : fileNames) {
			IvFile iv = IvFileFS.load(Paths.get(directory + fileName));
			System.out.println(iv);
		}

	}

	private void test2() {

		IvFile iv = IvFileFS.load(Paths.get("e:/Data/26-12-2017/1/2017-12-26_09-31-00.iv"));
		List<IvScan> scans = iv.getScans();
		for (IvScan scan : scans) {
			System.out.println(scan.getType());
		}

	}

	private void test3() {

		IvFile iv = IvFileFS.load(Paths.get("e:/Data/26-12-2017/1/2017-12-26_09-31-00.iv"));

		double[] power = new double[iv.getNumPoint()];

		List<IvScan> scans = iv.getScans();
		for (IvScan scan : scans) {
			if (scan.getType() == 1) {
				int re[] = scan.getRe();
				int im[] = scan.getIm();
				for (int i = 0; i < power.length; i++) {
					power[i] +=  (double)(re[i])*(double)(re[i]) + (double)(im[i])*(double)(im[i]);
				}
			}
		}
		
		for (int i = 0; i < power.length; i++) {
			System.out.println(power[i]);
		}

	}

	public static void main(String[] args) {

		TestIvApplication app = new TestIvApplication();
		app.test3();

	}

}
