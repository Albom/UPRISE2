package com.albom.application;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.albom.iion.isr.data.mu.MuSession;
import com.albom.iion.isr.data.mu.MuSessionFS;

public class MuTestApplication2 {

	private final String fileName = "h:/Data/MU/MI2972/MI2972.170905.160054";
	
	private void run() {
		int i = 0;
		MuSession s = null;
		while ((s = MuSessionFS.load(fileName, i)) != null) {
			System.out.println(String.valueOf(i) + "\t" + s.getHead().getMpulse().getPattern());
//			byte[] b = s.getHead().getHpnam().getBytes(StandardCharsets.UTF_8);
			System.out.println(s.getHead());
			System.out.println("\n\n");

			i++;
		}
	}
	
	public static void main(String[] args) {
		MuTestApplication2 app = new MuTestApplication2();
		app.run();
	}
	
}
