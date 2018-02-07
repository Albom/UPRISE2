package com.albom.iion.isr.data.mu;

import java.util.HashMap;

public class MuObservationMode {


	public static enum Mode {
		// @formatter:off
		RAW, 
		FFT_SPECTRA, 
		FFT_PARAMETERS, 
		FFT_SPECTRA_PARAMETERS, 
		SAD_CCFS, 
		SAD_PARAMETERS, 
		SAD_CCFS_PARAMETERS, 
		IONO_ACFS,
		IONO_ACFS_POWER,
		REMOVE_METEOR,
		POWER_PROFILE,
		POWER_REMOVE_METEOR,
		FFT_COMPLEX_SPECTRA,
		COHERENCE,
		UNKNOWN
		// @formatter:on
	}

	private static class ModeDescription {

		private Mode mode;
		private String text;

		private ModeDescription(Mode mode, String text) {
			this.mode = mode;
			this.text = text;
		}

		/**
		 * @return the mode
		 */
		public Mode getMode() {
			return mode;
		}

		/**
		 * @return the text
		 */
		public String getText() {
			return text;
		}

	}
	
	private static final HashMap<Integer, ModeDescription> map;
	static {
		map = new HashMap<Integer, ModeDescription>();
		map.put(Integer.valueOf(0),  new ModeDescription(Mode.RAW, "Raw data"));
		map.put(Integer.valueOf(1), new ModeDescription(Mode.FFT_SPECTRA, "FFT-spectra only"));
		map.put(Integer.valueOf(11), new ModeDescription(Mode.FFT_PARAMETERS, "FFT-parameters only"));
		map.put(Integer.valueOf(21), new ModeDescription(Mode.FFT_SPECTRA_PARAMETERS, "FFT-spectra & parameters"));
		map.put(Integer.valueOf(2), new ModeDescription(Mode.SAD_CCFS, "SAD-CCFs"));
		map.put(Integer.valueOf(12), new ModeDescription(Mode.SAD_PARAMETERS, "SAD-parameters"));
		map.put(Integer.valueOf(22), new ModeDescription(Mode.SAD_CCFS_PARAMETERS, "SAD-CCFs & parameters"));
		map.put(Integer.valueOf(3), new ModeDescription(Mode.IONO_ACFS, "iono.-ACFs"));
		map.put(Integer.valueOf(13), new ModeDescription(Mode.IONO_ACFS_POWER, "iono.-ACFs & power"));
		map.put(Integer.valueOf(23), new ModeDescription(Mode.REMOVE_METEOR, "(remove meteor)"));
		map.put(Integer.valueOf(4), new ModeDescription(Mode.POWER_PROFILE, "power profile"));
		map.put(Integer.valueOf(14), new ModeDescription(Mode.POWER_REMOVE_METEOR, "power (remove meteor)"));
		map.put(Integer.valueOf(5), new ModeDescription(Mode.FFT_COMPLEX_SPECTRA, "FFT-complex spectra"));
		map.put(Integer.valueOf(6), new ModeDescription(Mode.COHERENCE, "Coherence"));
		map.put(Integer.valueOf(99), new ModeDescription(Mode.UNKNOWN, "Unknown"));
	}

	private int mode = 99;

	/**
	 * 
	 * @param mode
	 *            <br>
	 *            0: Raw data <br>
	 *            1: FFT-spectra only <br>
	 *            11: FFT-parameters only <br>
	 *            21: FFT-spectra & parameters <br>
	 *            2: SAD-CCFs <br>
	 *            12: SAD-parameters <br>
	 *            22: SAD-CCFs & parameters <br>
	 *            3: iono.-ACFs <br>
	 *            13: iono.-ACFs & power <br>
	 *            23: (remove meteor) <br>
	 *            4: power profile <br>
	 *            14: power (remove meteor) <br>
	 *            5: FFT-complex spectra <br>
	 *            6: Coherence <br>
	 *            99: Unknown
	 * @throws Exception
	 */

	public MuObservationMode(int mode) throws Exception {
		if (map.get(mode) != null) {
			setMode(mode);
		} else {
			throw new Exception();
		}
	}

	public int getMode() {
		return mode;
	}

	public String getDescription() {
		return map.get(mode).getText();
	}

	private void setMode(int mode) {
		this.mode = mode;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + mode + ": " + map.get(mode).getText() + "]";
	}

	public Mode getEnumeratedMode() {
		return map.get(mode).getMode();
	}

}
