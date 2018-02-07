package com.albom.iion.isr.data.mu;

import java.util.Formatter;

public class MuEachHeader {

	private int beam;
	private int channel;
	private int height;

	public MuEachHeader(int beam, int channel, int height) {
		this.beam = beam;
		this.channel = channel;
		this.height = height;
	}

	/**
	 * @return the beam
	 */
	public int getBeam() {
		return beam;
	}

	/**
	 * @param beam
	 *            the beam to set
	 */
	public void setBeam(int beam) {
		this.beam = beam;
	}

	/**
	 * @return the channel
	 */
	public int getChannel() {
		return channel;
	}

	/**
	 * @param channel
	 *            the channel to set
	 */
	public void setChannel(int channel) {
		this.channel = channel;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		Formatter format = new Formatter(builder);
		format.format("%s[beam=%3d, channel=%3d, height=%5d]", this.getClass().getSimpleName(), beam, channel, height);
		format.close();
		return builder.toString();
	}

}
