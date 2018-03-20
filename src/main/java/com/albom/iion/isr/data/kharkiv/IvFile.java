package com.albom.iion.isr.data.kharkiv;

import java.time.LocalDateTime;
import java.util.List;

public class IvFile {

	public final static int VERSION = 1;
	
	private int version;
	private LocalDateTime date;
	private int nPoint;
	private int nScan;
	private int interval;

	private List<IvScan> scans = null;

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the date
	 */
	public LocalDateTime getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	/**
	 * @return the nPoint
	 */
	public int getNumPoint() {
		return nPoint;
	}

	/**
	 * @param nPoint
	 *            the nPoint to set
	 */
	public void setNumPoint(int nPoint) {
		this.nPoint = nPoint;
	}

	/**
	 * @return the nScan
	 */
	public int getNumScan() {
		return nScan;
	}

	/**
	 * @param nScan
	 *            the nScan to set
	 */
	public void setNumScan(int nScan) {
		this.nScan = nScan;
	}

	/**
	 * @return the interval
	 */
	public int getInterval() {
		return interval;
	}

	/**
	 * @param interval
	 *            the interval to set
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}

	/**
	 * @return the scans
	 */
	public List<IvScan> getScans() {
		return scans;
	}

	/**
	 * @param scans the scans to set
	 */
	public void setScans(List<IvScan> scans) {
		this.scans = scans;
	}
	
	public String toString(){
		return "[" + date + ", p=" + nPoint + ", s=" + nScan + ", i=" + interval + "]";
	}

}
