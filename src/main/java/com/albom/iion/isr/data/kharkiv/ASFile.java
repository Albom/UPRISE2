package com.albom.iion.isr.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ASFile {

	private Acf acfs[];
	private boolean isOpen;
	private String fileName;
	private LocalDateTime date;
	private int session;
	private int integrationTime;
	private double criticalFrequency;
	private double rnc[];
	private double rns[];
	private int nH;
	private String lastError = "";

	private final int nLag = 19;

	public class Acf {
		private int h;
		private double altitude;
		private double q;
		private double pShort;
		private double qShort;
		private double rc[];
		private double rs[];
		private double var[];

		Acf() {
		}

		Acf(int h) {
			setH(h);
		}

		public int getH() {
			return h;
		}

		public void setH(int h) {
			this.h = h;
		}

		public double getAltitude() {
			return altitude;
		}

		public void setAltitude(double altitude) {
			this.altitude = altitude;
		}

		public double getQ() {
			return q;
		}

		public void setQ(double q) {
			this.q = q;
		}

		public double getPShort() {
			return pShort;
		}

		public void setPShort(double pShort) {
			this.pShort = pShort;
		}

		public double getQShort() {
			return qShort;
		}

		public void setQShort(double qShort) {
			this.qShort = qShort;
		}

		public double[] getRc() {
			return rc;
		}

		public void setRc(double rc[]) {
			this.rc = rc;
		}

		public double[] getRs() {
			return rs;
		}

		public void setRs(double rs[]) {
			this.rs = rs;
		}

		public double[] getVar() {
			return var;
		}

		public void setVar(double var[]) {
			this.var = var;
		}

	}

	public ASFile() {
		setOpen(false);
	}

	public boolean load(String fileName) {
		Path file = Paths.get(fileName);
		try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.US_ASCII)) {

			setFileName(reader.readLine().trim());

			String date = reader.readLine().trim() + " " + reader.readLine().trim();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss");
			setDate(LocalDateTime.parse(date, formatter));

			setSession(Integer.parseInt(reader.readLine().trim()));
			setIntegrationTime(Integer.parseInt(reader.readLine().trim()));

			setCriticalFrequency(Double.parseDouble(reader.readLine().trim()));

			String buffer[] = reader.readLine().trim().split("\\s+");

			rnc = new double[19];
			rns = new double[19];
			for (int tau = 0; tau < nLag; tau++) {
				rnc[tau] = Double.parseDouble(buffer[tau]);
				rns[tau] = Double.parseDouble(buffer[tau + nLag]);
			}

			setNh(Integer.parseInt(reader.readLine().trim()));

			acfs = new Acf[nH];
			for (int i = 0; i < nH; i++) {
				buffer = reader.readLine().trim().split("\\s+");
				acfs[i] = new Acf();
				acfs[i].h = Integer.parseInt(buffer[0]);
				acfs[i].altitude = Double.parseDouble(buffer[1]);
				acfs[i].q = Double.parseDouble(buffer[2]);
				acfs[i].pShort = Double.parseDouble(buffer[3]);
				acfs[i].qShort = Double.parseDouble(buffer[4]);
				acfs[i].rc = new double[nLag];
				acfs[i].rs = new double[nLag];
				acfs[i].var = new double[nLag];
				for (int tau = 0; tau < nLag; tau++) {
					acfs[i].rc[tau] = Double.parseDouble(buffer[5 + tau]);
					acfs[i].rs[tau] = Double.parseDouble(buffer[5 + tau + nLag]);
					acfs[i].var[tau] = Double.parseDouble(buffer[5 + tau + 2 * nLag]);
				}
			}
			
			setOpen(true);
			return true;

		} catch (IOException e) {
			setLastError(e.toString());
			return false;
		}

	}

	public Acf[] getAcfs() {
		return acfs;
	}

	public void setAcfs(Acf acfs[]) {
		this.acfs = acfs;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public int getSession() {
		return session;
	}

	public void setSession(int session) {
		this.session = session;
	}

	public int getIntegrationTime() {
		return integrationTime;
	}

	public void setIntegrationTime(int integrationTime) {
		this.integrationTime = integrationTime;
	}

	public double getCriticalFrequency() {
		return criticalFrequency;
	}

	public void setCriticalFrequency(double criticalFrequency) {
		this.criticalFrequency = criticalFrequency;
	}

	public double[] getRnc() {
		return rnc;
	}

	public void setRnc(double rnc[]) {
		this.rnc = rnc;
	}

	public double[] getRns() {
		return rns;
	}

	public void setRns(double rns[]) {
		this.rns = rns;
	}

	public int getNh() {
		return nH;
	}

	public void setNh(int nH) {
		this.nH = nH;
	}

	public String getLastError() {
		return lastError;
	}

	private void setLastError(String lastError) {
		this.lastError = lastError;
	}

}
