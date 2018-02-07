package com.albom.iion.isr.data.mu;

public final class MuConstants {

	/** Bytes reserved in header */
	public final static int RESERVED_BYTES = 180;

	/** Max pulse sequence */
	public final static int MAX_PSEQ = 64;

	/** Max number of beams */
	public final static int MAX_BEAM = 256;

	/** Max number of TX frequencies */
	public final static int MAX_NTXFRQ = 5;

	/** Max number of analog channels */
	public final static int MAX_ANCHAN = 4;

	/** Max number of channels */
	public final static int MAX_CHAN = 29;

	/** Max number of FIR stages */
	public final static int MAX_FIR = 16;

	 /** Standard block size of header */
	 public final static int BLOCK_SIZE = 4480;
	
	// /** Number of spectral parameters */
	// public final static int NUMPAR = 6;
	//
	// /** Max length of comment */
	// public final static int MAX_COMMENT = 79;
	//
	// /** Max length of prgnam */
	// public final static int MAX_PRGNAM = 8;
	//
	// /** Max length of hpnam */
	// public final static int MAX_HPNAM = 6;
	//
	// /** Max length of oparam */
	// public final static int MAX_OPARAM = 15;
	//
	// /** Max number of height points */
	// public final static int MAX_NHIGH = 4096;
	//
	// /** Max number of height points by beams */
	// public final static int MAX_NHIGHBEAM = 4096;
	//
	// /** Max number of FFT points */
	// public final static int MAX_NDATA = 4096;
	//
	// /** Min number of FFT points */
	// public final static int MIN_NDATA = 64;
	//
	// /** Max number of Incoherent integrations */
	// public final static int MAX_NICOH = 32;
	//
	// /** Max points for fitting */
	// public final static int MAX_NFIT = 40;
	//
	// /** Number of sub-pulse lengths */
	// public final static int NUM_PULSES = 10;
	//
	// /** Number of FIR stages */
	// public final static int NUM_FIR = 16;
	//
	// /** Max pulse length */
	// public final static int MAX_PLEN = 32;
	//
	// /** Max TX pulse pattern length */
	// public final static int MAX_TXPLEN = 512;
	//
	// /** Max duty ratio (%) */
	// public final static int MAX_DUTY = 5;
	//
	// /** Idle time before transmission (us) */
	// public final static int IDLE_TIME = 10;

	private MuConstants() {
	}

}
