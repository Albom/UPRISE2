package com.albom.application;

import com.albom.iion.isr.processing.DirectProblem;

public class TestApplication {

	public static void main(String[] args) {
		// SNewFile s = new SNewFile();
		// SNewFileFS.loadFile(s, "d:/S190613.770");
		// System.out.print(s);
		// ProjectFactory projectFactory = new ProjectFactory();
		// ProjectDB project = projectFactory.getProject("d:/1.db3");
		// System.out.print("OK");

		// LocalDateTime[] t = new LocalDateTime[3];
		// for (int i = 0; i < 3; i++){
		// t[i] = LocalDateTime.now();
		// t[i] = t[i].plusSeconds(-62*i);
		// }
		//
		// TimeSorter tSort = new TimeSorter();
		// tSort.sort(t);
		// System.out.print(Arrays.toString(tSort.sort(t)));
		// System.out.print(KharkivRadar.WAVE_LENGTH);

		DirectProblem d = new DirectProblem();
		for (int f = 0; f < 5000; f += 200) {
			System.out.println(d.spectrum(0, 0, 1000, 1000, 1e5, false, f));
		}

	}

}
