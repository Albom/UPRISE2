package com.albom.iion.isr.data.mu;

import java.util.HashMap;

public class MuHeader2 {

	private int type = 0;

	private static final HashMap<Integer, String> map;
	static {
		map = new HashMap<Integer, String>();
		map.put(Integer.valueOf(0), "none");
		map.put(Integer.valueOf(1), "FFT type (no DC)");
		map.put(Integer.valueOf(11), "FFT type (DC)");
		map.put(Integer.valueOf(2), "SAD type");
	}

	/**
	 * 
	 * @param type
	 *            <br>
	 *            0: none <br>
	 *            1: FFT type (no DC) <br>
	 *            11: FFT type (DC) <br>
	 *            2: SAD type <br>
	 * @throws Exception
	 */

	public MuHeader2(int type) throws Exception {
		if (map.get(type) != null) {
			setType(type);
		} else {
			throw new Exception();
		}
	}

	public int getType() {
		return type;
	}

	private void setType(int type) {
		this.type = type;
	}

}
