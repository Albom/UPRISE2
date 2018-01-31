package com.albom.application;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import com.albom.iion.isr.data.CorrFileFS;
import com.albom.iion.isr.data.kharkiv.ASFile;
import com.albom.iion.isr.data.kharkiv.SNewFile;
import com.albom.iion.isr.data.kharkiv.SNewFileFS;
import com.albom.iion.isr.processing.ForwardProblem;
import com.albom.iion.isr.processing.TimeSorter;
import com.albom.utils.Directory;
import com.albom.utils.Time;

public class TestApplication {

	public static void main(String[] args) {
		// SNewFile s = new SNewFile();
		// SNewFileFS.loadFile(s, "d:/S190613.770");
		// System.out.print(s);
		// ProjectFactory projectFactory = new ProjectFactory();
		// ProjectDB project = projectFactory.getProject("d:/1.db3");
		// System.out.print("OK");

		// System.out.print(Arrays.toString(tSort.sort(t)));
		// System.out.print(KharkivRadar.WAVE_LENGTH);

		// DirectProblem d = new DirectProblem();
		// // for (int f = 0; f < 5000; f += 200) {
		// // System.out.println(d.spectrum(0, 0, 1000, 1000, 1e5, false, f));
		// // }
		// double[] acf = new double[19];
		// d.acf(600, 680, 30.555e-6, acf);
		//
		// double[] acf_teor = new double[acf.length];
		// int ti_c = 0, te_c = 0;
		//
		// double delta_g = 1e200;
		// for (int ti = 500; ti < 2000; ti += 100) {
		// for (int te = 500; te < 2000; te += 100) {
		//
		// if ((double) te / ti >= 1) {
		//
		// d.acf(ti, te, 30.555e-6, acf_teor); /// min количество!!!
		// double delta_l = 0;
		//
		// for (int tau = 0; tau < acf.length; tau++) {
		// delta_l += (acf[tau] - acf_teor[tau]) * (acf[tau] - acf_teor[tau]);
		// }
		// if (delta_l < delta_g) {
		// ti_c = ti;
		// te_c = te;
		// delta_g = delta_l;
		// }
		// }
		// }
		// }

		// System.out.println(ti_c + " " + te_c);

		// for (int tau = 0; tau < 19; tau++){
		// System.out.println(acf[tau]);
		// }

		// ASFile dat = new ASFile();
		// dat.load("d:/AS030608-030608.0002");
		// System.out.println(Arrays.toString(dat.getAcfs()[679].getVar()));

		// SNewFile s = new SNewFile();
		//
		// ArrayList<String> files =
		// Directory.listRecursively("D:/UPRISE-1.5.6/in/240915/");
		//
		// for (String f : files){
		// // System.out.println(f);
		// SNewFileFS.load(s, f);
		// }

		// CorrFileFS.loadFile(s, "d:/S190613.770");
		// System.out.println(s.getSession());

		// LocalDateTime[] t = new LocalDateTime[3];
		// for (int i = 0; i < 3; i++){
		// t[i] = LocalDateTime.now();
		// t[i] = t[i].plusSeconds(168*i);
		// }
		//
		// System.out.println( Arrays.toString( t ) );
		//
		// TimeSorter tSort = new TimeSorter();
		// LocalDateTime[] r = tSort.sort(t);
		// System.out.println( Arrays.toString( r ) );
		//
		// System.out.println( Arrays.toString( Time.diff(t) ) );
		// System.out.println( Arrays.toString( Time.diff(r) ) );

		ForwardProblem d = new ForwardProblem();
		LocalDateTime start = LocalDateTime.now();

		double[] acf = new double[19];
		d.acf(1000, 2000, 30.555e-6, acf);

		// double[] acf = { 1.00000E+00, 9.76086E-01, 8.81197E-01, 7.48226E-01,
		// 5.78550E-01, 3.90147E-01, 2.00562E-01,
		// 2.71604E-02, -1.17816E-01, -2.25389E-01, -2.89668E-01, -3.15264E-01,
		// -3.01961E-01, -2.58510E-01,
		// -1.96455E-01, -1.24621E-01, -6.57691E-02, -8.65948E-04, 1.89206E-02
		// };
		double[] acf2 = new double[19];
		// for (int i = 0; i < 3000; i++)
		// d.acf(1000, 2000, 30.555e-6, acf);

		// for (int i = 0; i < 18; i++) {
		// System.out.println(acf[i]);
		// }

		Path path = Paths.get("out.csv");
		double delta_c = 1e200;
		int ti_c = 500;
		int te_c = 500;
		try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"))) {
			for (int ti = 500; ti <= 4500; ti += 100) {
				for (int te = 500; te <= 4500; te += 100) {
					d.acf(ti, te, 30.555e-6, acf2);
					// d.acf(ti, te, 30.555e-6, acf);
					//
					double delta = 0;
					for (int i = 0; i < 18; i++) {
						delta += (acf[i] - acf2[i]) * (acf[i] - acf2[i]);
					}
					writer.write(String.valueOf(ti_c) + "; " + String.valueOf(te_c) + "; 1; "
							+ String.valueOf(ti - ti_c) + "; " + String.valueOf(te - te_c) + "; 0");
					writer.newLine();
					ti_c = ti;
					te_c = te;

					/*
					if (delta < delta_c) {
						// System.out.print(ti + "; " + te + "; " + delta);
						writer.write(String.valueOf(ti_c) + "; " + String.valueOf(te_c) + "; 1; "
								+ String.valueOf(ti - ti_c) + "; " + String.valueOf(te - te_c) + "; 0");
						// writer.write(ti + "; " + te + "; " + delta);
						writer.newLine();
						ti_c = ti;
						te_c = te;
						delta_c = delta;
					}*/
				}
				// writer.newLine();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		LocalDateTime end = LocalDateTime.now();

		System.out.println(Time.diff(start, end) + " s");

	}

}
