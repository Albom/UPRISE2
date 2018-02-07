package com.albom.iion.isr.data.mu;

public class MuDirection {

	private int azimuth;
	private int zenith;
	private int direction;

	public MuDirection(int direction) {

		this.direction = direction;

		if (direction == 0) {
			azimuth = 0;
			zenith = 0;
		} else {
			azimuth = ((direction - 1) % 72) * 5;
			zenith = ((direction < 1153) ? (direction - 1) / 72 + 1 : (((direction - 1) / 72) - 15) * 2 + 16);
		}

	}

	public MuDirection(int azimuth, int zenith) {

		this.azimuth = azimuth;
		this.zenith = zenith;

		if (azimuth < 0 || azimuth > 355 || zenith < 0 || azimuth % 5 != 0 || (zenith > 16 && (zenith - 16) % 2 != 0)) {
			direction = -1;
		}

		if (zenith == 0) {
			direction = 0;
		}

		direction = zenith <= 16 ? (zenith - 1) * 72 + azimuth / 5 + 1
				: 1152 + (zenith - 17) / 2 * 72 + azimuth / 5 + 1;

	}

	/**
	 * @return the azimuth
	 */
	public int getAzimuth() {
		return azimuth;
	}

	/**
	 * @return the zenith
	 */
	public int getZenith() {
		return zenith;
	}

	/**
	 * @return the direction
	 */
	public int getDirection() {
		return direction;
	}

	@Override
	public String toString() {
		return direction + " (" + azimuth + ", " + zenith + ")";
	}

}
