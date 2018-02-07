package com.albom.iion.isr.data.mu;

import java.util.HashMap;

public class MuHeader1 {

	private static final HashMap<Integer, Integer> map;
	static {
		map = new HashMap<Integer, Integer>();
		map.put(Integer.valueOf(0), Integer.valueOf(1000));
		map.put(Integer.valueOf(1000), Integer.valueOf(1000));
		map.put(Integer.valueOf(250), Integer.valueOf(250));
		map.put(Integer.valueOf(500), Integer.valueOf(500));
	}

	private int unitsOfHeight;

	/**
	 * 
	 * @param unitsOfHeight
	 *            <br>
	 *            0 (or 1000): 1 micro s<br>
	 *            250: 250 ns <br>
	 *            500: 500 ns
	 * @throws Exception
	 */

	public MuHeader1(int unitsOfHeight) throws Exception {
		if (map.get(unitsOfHeight) != null) {
			setUnitsOfHeight(unitsOfHeight);
		} else {
			throw new Exception();
		}
	}

	public int getUnitsOfHeight() {
		return unitsOfHeight;
	}
	
	public int getValue() {
		return map.get(unitsOfHeight);
	}

	private void setUnitsOfHeight(int unitsOfHeight) {
		this.unitsOfHeight = unitsOfHeight;
	}

}
