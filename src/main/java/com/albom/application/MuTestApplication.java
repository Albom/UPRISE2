package com.albom.application;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Locale;

import com.albom.iion.isr.data.mu.MuPulse;
import com.albom.iion.isr.data.mu.MuRawData;
import com.albom.iion.isr.data.mu.MuSession;
import com.albom.iion.isr.data.mu.MuSessionFS;

public class MuTestApplication {

	private void run() {

		int i = 0;
		MuSession s = null;
		while ((s = MuSessionFS.load("h:/Data/MU/MI2973/MI2973.170906.111537", i)) != null) {
			if (s.getHead().getMpulse().getPattern() == MuPulse.FOUR_PULSES) {
				break;
			}
			i++;
		}
		MuRawData[] data = (MuRawData[]) s.getData();

		int nH = 256;
		int nT = 512;
		double[][] tableRe = new double[nH][nT];
		double[][] tableIm = new double[nH][nT];

		for (int h = 0; h < nH; h++) {
			double[] re = data[h].getRe();
			double[] im = data[h].getIm();
			for (int t = 0; t < nT; t++) {
				tableRe[h][t] = re[t];
				tableIm[h][t] = im[t];
			}
		}

		// StringBuffer buffer = new StringBuffer();
		// for (int t = 0; t < nT; t++) {
		// for (int h = 0; h < nH; h++) {
		// buffer.append(String.format(Locale.US, "%8.4f;", tableRe[h][t]));
		// }
		// buffer.append("\n");
		// }
		//
		// try {
		// java.nio.file.Files.write(Paths.get("e:/MU_2017_09/acfs_1p_re.csv"),
		// buffer.toString().getBytes("utf-8"), StandardOpenOption.CREATE,
		// StandardOpenOption.TRUNCATE_EXISTING);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		for (int tau = 0; tau < 36; tau++) {
			for (int h = 0; h < 12; h++) {
				//
				double r = 0;
				// double b0Re = 0;
				// double b6Re = 0;
				// double b0Im = 0;
				// double b6Im = 0;
				//
				for (int t = 0; t < nT; t++) {
					r += tableRe[h][t] * tableRe[h + tau][t];// + tableIm[h][t] *
															// tableIm[h+1][t];
					// b0Re += tableRe[h][t];
					// b6Re += tableRe[h + 6][t];
					// b0Im += tableIm[h][t];
					// b6Im += tableIm[h + 6][t];
				}
				System.out.print(String.format(Locale.US, "%6.1f", r));
			}
			System.out.println("");
		}

	}

	public static void main(String[] args) {
		MuTestApplication app = new MuTestApplication();
		app.run();
	}

}
