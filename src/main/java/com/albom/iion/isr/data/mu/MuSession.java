package com.albom.iion.isr.data.mu;

public class MuSession {

	private MuHeader head;
	private MuData[] data;

	public MuSession(MuHeader head, MuData[] data) {
		this.head = head;
		this.data = data;
	}

	/**
	 * @return the head
	 */
	public MuHeader getHead() {
		return head;
	}


	/**
	 * @return the data
	 */
	public MuData[] getData() {
		return data;
	}


}
