package com.albom.application;

import com.albom.iion.isr.data.SNewFile;
import com.albom.iion.isr.data.SNewFileFS;

public class Application {

	public static void main(String[] args) {

		SNewFile s = new SNewFile();
		SNewFileFS.loadFile(s, "d:/S190613.770");
		System.out.print(s);

	}

}
