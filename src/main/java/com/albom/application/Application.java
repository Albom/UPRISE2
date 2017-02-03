package com.albom.application;

import com.albom.iion.isr.data.ProjectDB;
import com.albom.iion.isr.data.ProjectFactory;
import com.albom.iion.isr.data.SNewFile;
import com.albom.iion.isr.data.SNewFileFS;

public class Application {

	public static void main(String[] args) {

		// SNewFile s = new SNewFile();
		// SNewFileFS.loadFile(s, "d:/S190613.770");
		// System.out.print(s);
		ProjectFactory projectFactory = new ProjectFactory();
		ProjectDB project = projectFactory.getProject("d:/1.db3");
		System.out.print("OK");

	}

}
