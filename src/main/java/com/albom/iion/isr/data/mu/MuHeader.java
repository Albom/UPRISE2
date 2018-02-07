package com.albom.iion.isr.data.mu;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Locale;

public class MuHeader {

	private static final String GR_1 = "AAAABBBBCCCCDDDDEEEEFFFFF";
	private static final String GR_2 = "1234123412341234123412345";

	/**
	 * Length of a data block
	 */
	private int lnblk;

	/**
	 * Number of total blocks (Header, spectra & parameters)
	 */
	private int ntblk;

	/**
	 * Number of data blocks (Spectra only)
	 */
	private int ndblk;

	/**
	 * Length of segment
	 */
	private int lnseg;

	/**
	 * Length of record header
	 */
	private int lnhead;

	/**
	 * Number of header blocks
	 */
	private int nhblk;

	/**
	 * Data taking (Signal processing) program name (8 bytes)
	 */
	private String prgnam;

	/**
	 * Parameter file load time (24 bytes)
	 */
	private LocalDateTime ldtime;

	/**
	 * Data taking program number (since control program started) (not use)
	 */
	private int nprog;

	/**
	 * Record start time [DD-MMM-YYYY hh:mm:ss.ss]
	 */
	private LocalDateTime recsta;

	/**
	 * Record end time [hh:mm:ss.ss]
	 */
	private LocalTime recend;

	/**
	 * Record number (since observation program started)
	 */
	private int irec;

	/**
	 * Total record number (since control program started) (not use)
	 */
	private int itrec;

	/**
	 * Observation mode
	 */
	private MuObservationMode mobs;

	/**
	 * Usage of each header 1 (units of height)
	 */
	private MuHeader1 mhead1;

	/**
	 * Usage of each header 2
	 */
	private MuHeader2 mhead2;

	/**
	 * Maximum number of data points in all combined channels (or ACF lags)
	 */
	private int ndata;

	/**
	 * Number of result data
	 */
	private int nrdata;

	/**
	 * Number of total segments
	 */
	private int ntseg;

	/**
	 * Number of height points
	 */
	private int nhigh;

	/**
	 * Number of beam directions
	 */
	private int nbeam;

	/**
	 * Number of combined channels
	 */
	private int nchan;

	/**
	 * Number of data in same height
	 */
	private int nccf;

	/**
	 * IPP (micro s)
	 */
	private int ipp;

	/**
	 * Sample start time (micro s)
	 */
	private int jstart;

	/**
	 * Sample interval (micro s)
	 */
	private int jsint;

	/**
	 * Maximum number of coherent integrations in all combined channels
	 */
	private int ncoh;

	/**
	 * Number of incoherent integrations
	 */
	private int nicoh;

	/**
	 * Data type (FFT-mode)
	 */
	private MuDataType mtype;

	/**
	 * Multi-pulse pattern (32 bits)
	 */
	private MuPulse mpulse;

	/**
	 * Lag number of each ACF point (21 words)
	 */
	private int[] macf = new int[21];

	/**
	 * Beam directional number in first 16 beams (16 words)
	 */
	private int[] ibeam = new int[21];

	/**
	 * Number of fitting points in dopplfit
	 */
	private int nfit;

	/**
	 * Length of a sub-pulse (micro s) (-1: 0.5micro s)
	 */
	private int lsubp;

	/**
	 * Number of sub-pulse
	 */
	private int nsubp;

	/**
	 * Beam scanning mode <br>
	 * 1: every ISPL (unavailable) <br>
	 * 0: every IPP
	 */
	private int mscan;

	/**
	 * HP parameter-file name (<b>8</b> bytes)
	 */
	private String hpnam;

	private int nomode;

	private int nehead;

	/**
	 * Number of sum (ACF method) (16 words)
	 */
	private int[] nicohm = new int[16];

	/**
	 * Number of sample points (ACF method)
	 */
	private int nsampl;

	/**
	 * Reserved for the future (180 bytes)
	 */
	private byte[] reserv = new byte[MuConstants.RESERVED_BYTES];

	/**
	 * Observation parameter name (16 bytes)
	 */
	private String oparam;

	/**
	 * Program Version
	 */
	private int iprver;

	/**
	 * Record start time (s) (since epoch)
	 */
	private int ista;

	/**
	 * Record start time shorter than 1 sec (micro s)
	 */
	private int istaus;

	/**
	 * Record end time (s) (since epoch)
	 */
	private int iend;

	/**
	 * Record end time shorter than 1 sec (micro s)
	 */
	private int iendus;

	/**
	 * Number of pulse sequencies (1-64)
	 */
	private int npseq;

	/**
	 * Transmit pulse pattern (32 bits x 64)
	 */
	private int[] itxcod = new int[MuConstants.MAX_PSEQ];

	/**
	 * Decoding code length for all channels
	 */
	private int ldcdal;

	/**
	 * Number of pulse decoding sequences for all channels
	 */
	private int npsqal;

	/**
	 * Pulse decoding pattern for all channels (32 bits x 64)
	 */
	private int[] idcdal = new int[MuConstants.MAX_PSEQ];

	/**
	 * Beam steering interval (0: one beam, 1: IPP, 2: 2 IPP, 3: FFT)
	 */
	private int isteer;

	/**
	 * Beam direction (2 bytes x 256)
	 */
	private int[] ibeam2 = new int[MuConstants.MAX_BEAM];

	/**
	 * Beam shape
	 */
	private int ibshap;

	/**
	 * Beam azimuth offset (0.01 degree unit)
	 */
	private int iazoff;

	/**
	 * Beam zenith offset (0.01 degree unit)
	 */
	private int izeoff;

	/**
	 * Polarization (1: right circular fixed)
	 */
	private int ipolar;

	/**
	 * Number of TX frequencies
	 */
	private int ntxfrq;

	/**
	 * TX frequency offset (5 words)
	 */
	private float[] txfreq = new float[MuConstants.MAX_NTXFRQ];

	/**
	 * Gain correction of TX source signal
	 */
	private int igain;

	/**
	 * TX attenuator
	 */
	private int itxatt;

	/**
	 * RX attenuator (4 words)
	 */
	private int[] irxatt = new int[MuConstants.MAX_ANCHAN];

	/**
	 * TX on(1)/off(0) (1-25 bits) and TX module No. (26-30 bits; 0: all)
	 */
	private int itxon;

	/**
	 * RX on(1)/off(0) (4 words) RX No. 1-4 means Channel No. 26-29
	 */
	private int[] irxon = new int[MuConstants.MAX_ANCHAN];

	/**
	 * RX module selection (2 bytes x (25 + 1), 0: all modules)
	 */
	private int[] irxsel = new int[26];

	/**
	 * Selection of filter (same as PIO)
	 */
	private int ifiltr;

	/**
	 * Range zero correction (ns)
	 */
	private int irngzr;

	/**
	 * Sample start time (256 words: unit of sub-pulse/4)
	 */
	private int[] istart = new int[MuConstants.MAX_BEAM];

	/**
	 * Reception sequence (Dummy)
	 */
	private int irxseq;

	/**
	 * Channel number in digital combine (32 bits x 29)
	 */
	private int[] ichan = new int[MuConstants.MAX_CHAN];

	/**
	 * Number of coherent integrations for each combined channel (29 words)
	 */
	private int[] ncoh2 = new int[MuConstants.MAX_CHAN];

	/**
	 * Number of FFT points for each combined channel (29 words)
	 */
	private int[] nfft = new int[MuConstants.MAX_CHAN];

	/**
	 * Number of data points for each combined channel (29 words)
	 */
	private int[] ndata2 = new int[MuConstants.MAX_CHAN];

	/**
	 * Lower and upper boundary of FFT number in each combined channel <br>
	 * (2 bytes x 2 x 29)
	 */
	private int[][] ifft1 = new int[MuConstants.MAX_CHAN][2];

	/**
	 * Lower and upper boundary of FFT number in each combined channel <br>
	 * (2 bytes x 2 x 29)
	 */
	private int[][] ifft2 = new int[MuConstants.MAX_CHAN][2];

	/**
	 * Lower and upper boundary of FFT number in each combined channel <br>
	 * (2 bytes x 2 x 29)
	 */
	private int[][] ifft3 = new int[MuConstants.MAX_CHAN][2];

	/**
	 * RX frequency offset for each combined channel (29 words)
	 */
	private float[] rxfreq = new float[MuConstants.MAX_CHAN];

	/**
	 * FIR coefficient in TX (2 bytes x 16)
	 */
	private int itxfir[] = new int[MuConstants.MAX_FIR];

	/**
	 * Gain adjustment of FIR filter in RX for each combined channel <br>
	 * (2 bytes x 2 x 29)
	 */
	private int[][] igafir = new int[MuConstants.MAX_CHAN][2];

	/**
	 * CIC interpolation pattern in TX (0-15)
	 */
	private int intptn;

	/**
	 * CIC interpolation rate in TX (1-640)
	 */
	private int intrat;

	/**
	 * Number of CIC filter in TX (1-10)
	 */
	private int ntxcic;

	/**
	 * Gain adjustment of CIC filter in TX (log2 G)
	 */
	private int igacic;

	/**
	 * Number of CIC filter in RX for each combined channel (29 words)
	 */
	private int[] nrxcic = new int[MuConstants.MAX_CHAN];

	/**
	 * CIC cropping rate in RX for each combined channel (29 words)
	 */
	private int[] icrrat = new int[MuConstants.MAX_CHAN];

	/**
	 * Above sea level (m) \hfill ...\ Memo.
	 */
	private float sealvl;

	/**
	 * Header flag
	 */
	private int iheadf;

	/**
	 * Comment by user (80 bytes)
	 */
	private String coment;

	/**
	 * Number of FFT points to calculate coherence
	 */
	private int nfftc;

	/**
	 * Number of baselines
	 */
	private int nbls;

	/**
	 * Channel number in coherence calculation (32 bits)
	 */
	private int ichanc;

	/**
	 * User header
	 */
	private byte[] usrhdr = new byte[208];

	/**
	 * Get length of a data block
	 */
	public int getLnblk() {
		return lnblk;
	}

	/**
	 * Set length of a data block
	 */
	public void setLnblk(int lnblk) {
		this.lnblk = lnblk;
	}

	/**
	 * Get number of total blocks (Header, spectra & parameters)
	 */
	public int getNtblk() {
		return ntblk;
	}

	/**
	 * Set number of total blocks (Header, spectra & parameters)
	 */
	public void setNtblk(int ntblk) {
		this.ntblk = ntblk;
	}

	/**
	 * Get number of data blocks (Spectra only)
	 */
	public int getNdblk() {
		return ndblk;
	}

	/**
	 * Set number of data blocks (Spectra only)
	 */
	public void setNdblk(int ndblk) {
		this.ndblk = ndblk;
	}

	public int getLnseg() {
		return lnseg;
	}

	public void setLnseg(int lnseg) {
		this.lnseg = lnseg;
	}

	public int getLnhead() {
		return lnhead;
	}

	public void setLnhead(int lnhead) {
		this.lnhead = lnhead;
	}

	public int getNhblk() {
		return nhblk;
	}

	public void setNhblk(int nhblk) {
		this.nhblk = nhblk;
	}

	public String getPrgnam() {
		return prgnam;
	}

	public void setPrgnam(String prgnam) {
		this.prgnam = prgnam;
	}

	public LocalDateTime getLdtime() {
		return ldtime;
	}

	public void setLdtime(LocalDateTime ldtime) {
		this.ldtime = ldtime;
	}

	public int getNprog() {
		return nprog;
	}

	public void setNprog(int nprog) {
		this.nprog = nprog;
	}

	public LocalDateTime getRecsta() {
		return recsta;
	}

	public void setRecsta(LocalDateTime recsta) {
		this.recsta = recsta;
	}

	public LocalTime getRecend() {
		return recend;
	}

	public void setRecend(LocalTime recend) {
		this.recend = recend;
	}

	public int getIrec() {
		return irec;
	}

	public void setIrec(int irec) {
		this.irec = irec;
	}

	public int getItrec() {
		return itrec;
	}

	public void setItrec(int itrec) {
		this.itrec = itrec;
	}

	public MuObservationMode getMobs() {
		return mobs;
	}

	public void setMobs(MuObservationMode mobs) {
		this.mobs = mobs;
	}

	public MuHeader1 getMhead1() {
		return mhead1;
	}

	public void setMhead1(MuHeader1 mhead1) {
		this.mhead1 = mhead1;
	}

	public MuHeader2 getMhead2() {
		return mhead2;
	}

	public void setMhead2(MuHeader2 mhead2) {
		this.mhead2 = mhead2;
	}

	public int getNrdata() {
		return nrdata;
	}

	public void setNrdata(int nrdata) {
		this.nrdata = nrdata;
	}

	public int getNhigh() {
		return nhigh;
	}

	public void setNhigh(int nhigh) {
		this.nhigh = nhigh;
	}

	public int getNtseg() {
		return ntseg;
	}

	public void setNtseg(int ntseg) {
		this.ntseg = ntseg;
	}

	public int getNbeam() {
		return nbeam;
	}

	public void setNbeam(int nbeam) {
		this.nbeam = nbeam;
	}

	public int getNchan() {
		return nchan;
	}

	public void setNchan(int nchan) {
		this.nchan = nchan;
	}

	public int getNccf() {
		return nccf;
	}

	public void setNccf(int nccf) {
		this.nccf = nccf;
	}

	public int getIpp() {
		return ipp;
	}

	public void setIpp(int ipp) {
		this.ipp = ipp;
	}

	public int getJstart() {
		return jstart;
	}

	public void setJstart(int jstart) {
		this.jstart = jstart;
	}

	public int getJsint() {
		return jsint;
	}

	public void setJsint(int jsint) {
		this.jsint = jsint;
	}

	public int getNcoh() {
		return ncoh;
	}

	public void setNcoh(int ncoh) {
		this.ncoh = ncoh;
	}

	public int getNicoh() {
		return nicoh;
	}

	public void setNicoh(int nicoh) {
		this.nicoh = nicoh;
	}

	public MuDataType getMtype() {
		return mtype;
	}

	public void setMtype(MuDataType mtype) {
		this.mtype = mtype;
	}

	public int getNdata() {
		return ndata;
	}

	public void setNdata(int ndata) {
		this.ndata = ndata;
	}

	public MuPulse getMpulse() {
		return mpulse;
	}

	public void setMpulse(MuPulse mpulse) {
		this.mpulse = mpulse;
	}

	public int[] getMacf() {
		return macf;
	}

	public void setMacf(int[] macf) {
		this.macf = macf;
	}

	public int[] getIbeam() {
		return ibeam;
	}

	public void setIbeam(int[] ibeam) {
		this.ibeam = ibeam;
	}

	public int getNfit() {
		return nfit;
	}

	public void setNfit(int nfit) {
		this.nfit = nfit;
	}

	public int getLsubp() {
		return lsubp;
	}

	public void setLsubp(int lsubp) {
		this.lsubp = lsubp;
	}

	public int getNsubp() {
		return nsubp;
	}

	public void setNsubp(int nsubp) {
		this.nsubp = nsubp;
	}

	public int getMscan() {
		return mscan;
	}

	public void setMscan(int mscan) {
		this.mscan = mscan;
	}

	public String getHpnam() {
		return hpnam;
	}

	public void setHpnam(String hpnam) {
		this.hpnam = hpnam;
	}

	public int getNomode() {
		return nomode;
	}

	public void setNomode(int nomode) {
		this.nomode = nomode;
	}

	public int getNehead() {
		return nehead;
	}

	public void setNehead(int nehead) {
		this.nehead = nehead;
	}

	public int[] getNicohm() {
		return nicohm;
	}

	public void setNicohm(int[] nicohm) {
		this.nicohm = nicohm;
	}

	public int getNsampl() {
		return nsampl;
	}

	public void setNsampl(int nsampl) {
		this.nsampl = nsampl;
	}

	public byte[] getReserv() {
		return reserv;
	}

	public void setReserv(byte[] reserv) {
		this.reserv = reserv;
	}

	public String getOparam() {
		return oparam;
	}

	public void setOparam(String oparam) {
		this.oparam = oparam;
	}

	public int getIprver() {
		return iprver;
	}

	public void setIprver(int iprver) {
		this.iprver = iprver;
	}

	public int getIsta() {
		return ista;
	}

	public void setIsta(int ista) {
		this.ista = ista;
	}

	public int getIstaus() {
		return istaus;
	}

	public void setIstaus(int istaus) {
		this.istaus = istaus;
	}

	public int getIend() {
		return iend;
	}

	public void setIend(int iend) {
		this.iend = iend;
	}

	public int getIendus() {
		return iendus;
	}

	public void setIendus(int iendus) {
		this.iendus = iendus;
	}

	public int getNpseq() {
		return npseq;
	}

	public void setNpseq(int npseq) {
		this.npseq = npseq;
	}

	public int[] getItxcod() {
		return itxcod;
	}

	public void setItxcod(int[] itxcod) {
		this.itxcod = itxcod;
	}

	public int getLdcdal() {
		return ldcdal;
	}

	public void setLdcdal(int ldcdal) {
		this.ldcdal = ldcdal;
	}

	public int getNpsqal() {
		return npsqal;
	}

	public void setNpsqal(int npsqal) {
		this.npsqal = npsqal;
	}

	public int[] getIdcdal() {
		return idcdal;
	}

	public void setIdcdal(int[] idcdal) {
		this.idcdal = idcdal;
	}

	public int getIsteer() {
		return isteer;
	}

	public void setIsteer(int isteer) {
		this.isteer = isteer;
	}

	public int[] getIbeam2() {
		return ibeam2;
	}

	public void setIbeam2(int[] ibeam2) {
		this.ibeam2 = ibeam2;
	}

	public int getIbshap() {
		return ibshap;
	}

	public void setIbshap(int ibshap) {
		this.ibshap = ibshap;
	}

	public int getIazoff() {
		return iazoff;
	}

	public void setIazoff(int iazoff) {
		this.iazoff = iazoff;
	}

	public int getIzeoff() {
		return izeoff;
	}

	public void setIzeoff(int izeoff) {
		this.izeoff = izeoff;
	}

	public int getIpolar() {
		return ipolar;
	}

	public void setIpolar(int ipolar) {
		this.ipolar = ipolar;
	}

	public int getNtxfrq() {
		return ntxfrq;
	}

	public void setNtxfrq(int ntxfrq) {
		this.ntxfrq = ntxfrq;
	}

	public float[] getTxfreq() {
		return txfreq;
	}

	public void setTxfreq(float[] txfreq) {
		this.txfreq = txfreq;
	}

	public int getIgain() {
		return igain;
	}

	public void setIgain(int igain) {
		this.igain = igain;
	}

	public int getItxatt() {
		return itxatt;
	}

	public void setItxatt(int itxatt) {
		this.itxatt = itxatt;
	}

	public int[] getIrxatt() {
		return irxatt;
	}

	public void setIrxatt(int[] irxatt) {
		this.irxatt = irxatt;
	}

	public int getItxon() {
		return itxon;
	}

	public void setItxon(int itxon) {
		this.itxon = itxon;
	}

	public int[] getIrxon() {
		return irxon;
	}

	public void setIrxon(int[] irxon) {
		this.irxon = irxon;
	}

	public int[] getIrxsel() {
		return irxsel;
	}

	public void setIrxsel(int[] irxsel) {
		this.irxsel = irxsel;
	}

	public int getIfiltr() {
		return ifiltr;
	}

	public void setIfiltr(int ifiltr) {
		this.ifiltr = ifiltr;
	}

	public int getIrngzr() {
		return irngzr;
	}

	public void setIrngzr(int irngzr) {
		this.irngzr = irngzr;
	}

	public int[] getIstart() {
		return istart;
	}

	public void setIstart(int[] istart) {
		this.istart = istart;
	}

	public int getIrxseq() {
		return irxseq;
	}

	public void setIrxseq(int irxseq) {
		this.irxseq = irxseq;
	}

	public int[] getIchan() {
		return ichan;
	}

	public void setIchan(int[] ichan) {
		this.ichan = ichan;
	}

	public int[] getNcoh2() {
		return ncoh2;
	}

	public void setNcoh2(int[] ncoh2) {
		this.ncoh2 = ncoh2;
	}

	public int[] getNfft() {
		return nfft;
	}

	public void setNfft(int[] nfft) {
		this.nfft = nfft;
	}

	public int[] getNdata2() {
		return ndata2;
	}

	public void setNdata2(int[] ndata2) {
		this.ndata2 = ndata2;
	}

	public int[][] getIfft1() {
		return ifft1;
	}

	public void setIfft1(int[][] ifft1) {
		this.ifft1 = ifft1;
	}

	public int[][] getIfft2() {
		return ifft2;
	}

	public void setIfft2(int[][] ifft2) {
		this.ifft2 = ifft2;
	}

	public int[][] getIfft3() {
		return ifft3;
	}

	public void setIfft3(int[][] ifft3) {
		this.ifft3 = ifft3;
	}

	public float[] getRxfreq() {
		return rxfreq;
	}

	public void setRxfreq(float[] rxfreq) {
		this.rxfreq = rxfreq;
	}

	public int[] getItxfir() {
		return itxfir;
	}

	public void setItxfir(int itxfir[]) {
		this.itxfir = itxfir;
	}

	public int[][] getIgafir() {
		return igafir;
	}

	public void setIgafir(int[][] igafir) {
		this.igafir = igafir;
	}

	public int getIntptn() {
		return intptn;
	}

	public void setIntptn(int intptn) {
		this.intptn = intptn;
	}

	public int getIntrat() {
		return intrat;
	}

	public void setIntrat(int intrat) {
		this.intrat = intrat;
	}

	public int getNtxcic() {
		return ntxcic;
	}

	public void setNtxcic(int ntxcic) {
		this.ntxcic = ntxcic;
	}

	public int getIgacic() {
		return igacic;
	}

	public void setIgacic(int igacic) {
		this.igacic = igacic;
	}

	public int[] getNrxcic() {
		return nrxcic;
	}

	public void setNrxcic(int[] nrxcic) {
		this.nrxcic = nrxcic;
	}

	public int[] getIcrrat() {
		return icrrat;
	}

	public void setIcrrat(int[] icrrat) {
		this.icrrat = icrrat;
	}

	public float getSealvl() {
		return sealvl;
	}

	public void setSealvl(float sealvl) {
		this.sealvl = sealvl;
	}

	public int getIheadf() {
		return iheadf;
	}

	public void setIheadf(int iheadf) {
		this.iheadf = iheadf;
	}

	public String getComent() {
		return coment;
	}

	public void setComent(String coment) {
		this.coment = coment;
	}

	public int getNfftc() {
		return nfftc;
	}

	public void setNfftc(int nfftc) {
		this.nfftc = nfftc;
	}

	public int getNbls() {
		return nbls;
	}

	public void setNbls(int nbls) {
		this.nbls = nbls;
	}

	public int getIchanc() {
		return ichanc;
	}

	public void setIchanc(int ichanc) {
		this.ichanc = ichanc;
	}

	public byte[] getUsrhdr() {
		return usrhdr;
	}

	public void setUsrhdr(byte[] usrhdr) {
		this.usrhdr = usrhdr;
	}

	@Override
	public String toString() {

		final class MacfValues {

			public String toString() {
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < macf.length; i++) {
					builder.append("macf[");
					builder.append(String.format("%02d", i));
					builder.append("] = ");
					builder.append(macf[i]);
					builder.append("\n");
				}
				return builder.toString();
			}

		}

		final class IbeamValues {

			public String toString() {
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < (nbeam < ibeam.length ? nbeam : ibeam.length); i++) {
					builder.append("ibeam[");
					builder.append(String.format("%2d", i));
					builder.append("] = ");
					builder.append(new MuDirection(ibeam[i]));
					builder.append("\n");
				}
				return builder.toString();
			}

		}

		final class ItxValues {

			public String toString() {
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < npseq; i++) {
					builder.append("itxcod[");
					builder.append(String.format("%2d", i));
					builder.append("] = ");
					String bin = Integer.toBinaryString(itxcod[i]);
					if (bin.length() > nsubp) {
						bin = bin.substring(bin.length() - nsubp);
					}
					builder.append(bin);
					builder.append(" (");
					builder.append(String.format("0x%04x", itxcod[i]));
					builder.append(")\n");
				}
				return builder.toString();
			}

		}

		final class IdcValues {

			public String toString() {
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < npsqal; i++) {
					builder.append("idcdal[");
					builder.append(String.format("%2d", i));
					builder.append("] = ");
					String bin = Integer.toBinaryString(idcdal[i]);
					if (bin.length() > ldcdal) {
						bin = bin.substring(bin.length() - ldcdal);
					}
					builder.append(bin);
					builder.append(" (");
					builder.append(String.format("0x%04x", idcdal[i]));
					builder.append(")\n");
				}
				return builder.toString();
			}

		}

		final class Ibeam2Values {

			public String toString() {
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < nbeam; i++) {
					builder.append("ibeam2[");
					builder.append(String.format("%2d", i));
					builder.append("] = ");
					builder.append(new MuDirection(ibeam2[i]));
					builder.append("\n");
				}
				return builder.toString();
			}

		}
		double duty = (double) (jsint * nsubp) / ipp * 100.0;

		StringBuilder builder = new StringBuilder();

		// @formatter:off
		builder.append("[").append(this.getClass().getSimpleName()).append("\n");
		builder.append("lnblk  = ").append(lnblk).append("\n");
		builder.append("ntblk  = ").append(ntblk).append("\n");
		builder.append("ndblk  = ").append(ndblk).append("\n");
		builder.append("lnseg  = ").append(lnseg).append("\n");
		builder.append("lnhead = ").append(lnhead).append("\n");
		builder.append("nhblk  = ").append(nhblk).append("\n");
		builder.append("prgnam = ").append(prgnam).append("\n");
		builder.append("ldtime = ").append(MuDateTime.toString(ldtime)).append("\n");
		builder.append("nprog  = ").append(nprog).append("\n");
		builder.append("recsta = ").append(MuDateTime.toString(recsta)).append(", recend = ").append(MuTime.toString(recend)).append("\n");
		builder.append("irec   = ").append(irec).append("\n");
		builder.append("itrec  = ").append(itrec).append("\n");
		builder.append("mobs   = ").append(mobs.getMode()).append("\n");
		builder.append("mhead1 = ").append(mhead1.getUnitsOfHeight()).append("\n");
		builder.append("mhead2 = ").append(mhead2.getType()).append("\n");
		builder.append("ndata  = ").append(ndata).append("\n");
		builder.append("nrdata = ").append(nrdata).append("\n");
		builder.append("ntseg  = ").append(ntseg).append("\n");
		builder.append("nhigh  = ").append(nhigh).append("\n");
		builder.append("nbeam  = ").append(nbeam).append("\n");
		builder.append("nchan  = ").append(nchan).append("\n");
		builder.append("nccf   = ").append(nccf).append("\n");
		builder.append("ipp    = ").append(ipp).append("\n");
		builder.append("duty   = ").append(String.format(Locale.ENGLISH, "%5.2f", duty)).append("(%)").append("\n");
		builder.append("jstart = ").append(jstart).append("\n");
		builder.append("jsint  = ").append(jsint).append("\n");
		builder.append("ncoh   = ").append(ncoh).append("\n");
		builder.append("nicoh  = ").append(nicoh).append("\n");
		builder.append("mtype  = ").append(mtype).append("\n");
		builder.append("mpulse = ").append(mpulse).append(" (").append(String.format(Locale.ENGLISH, "0x%04x", mpulse.getPattern())).append(")").append("\n");
		builder.append(new MacfValues()).append("\n");
		builder.append(new IbeamValues()).append("\n");
		builder.append("nfit   = ").append(nfit).append("\n");
		builder.append("lsubp  = ").append(lsubp).append("\n");
		builder.append("nsubp  = ").append(nsubp).append("\n");
		builder.append("mscan  = ").append(mscan).append("\n");
		builder.append("hpnam  = ").append(hpnam).append("\n");
		builder.append("nomode = ").append(nomode).append("\n");
		builder.append("nehead = ").append(nehead).append("\n");
		builder.append("nicohm = ").append(Arrays.toString(nicohm).replaceAll("\\[", "").replaceAll("\\]", "")).append("\n");
		builder.append("nsampl = ").append(nsampl).append("\n");
		builder.append("-------------------------------\n");
		builder.append("oparam = ").append(oparam).append("\n");
		builder.append("iprver = ").append(iprver).append("\n");
		builder.append("ista   = ").append(ista).append("\n");
		builder.append("istaus = ").append(istaus).append("\n");
		builder.append("iend   = ").append(iend).append("\n");
		builder.append("iendus = ").append(iendus).append("\n");
		builder.append("npseq  = ").append(npseq).append("\n\n");
		builder.append(new ItxValues()).append("\n");
		builder.append("ldcdal = ").append(ldcdal).append("\n");
		builder.append("npsqal = ").append(npsqal).append("\n");
		builder.append(new IdcValues()).append("\n");
		builder.append("isteer = ").append(isteer).append("\n");
		builder.append(new Ibeam2Values()).append("\n");
		builder.append("ibshap = ").append(ibshap).append("\n");
		builder.append("iazoff = ").append(iazoff).append("\n");
		builder.append("izeoff = ").append(izeoff).append("\n");
		builder.append("ipolar = ").append(ipolar).append("\n");
		builder.append("ntxfrq = ").append(ntxfrq).append("\n");
		builder.append("txfreq = ").append(Arrays.toString(Arrays.copyOfRange(txfreq, 0, ntxfrq)).replaceAll("\\[", "").replaceAll("\\]", "")).append("\n");
		builder.append("igain  = ").append(igain).append("\n");
		builder.append("itxatt = ").append(itxatt).append("\n");
		builder.append("irxatt = ").append(Arrays.toString(irxatt).replaceAll("\\[", "").replaceAll("\\]", "")).append("\n");
		builder.append(String.format("                    %s\n", GR_1));
		builder.append(String.format("                    %s\n", GR_2));
		builder.append("itxon  = ").append(String.format("0x%08x", itxon)).append(" ").append(new StringBuilder(String.format("%25s", Integer.toBinaryString(itxon)).replaceAll(" ", "0").substring(0, 25)).reverse()).append("\n");
		builder.append("]");
		// builder.append(irxon
		// @formatter:on

		return builder.toString();
	}

}
