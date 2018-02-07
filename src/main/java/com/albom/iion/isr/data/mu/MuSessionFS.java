package com.albom.iion.isr.data.mu;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;

public abstract class MuSessionFS {

	public static MuSession load(String fileName) {
		return load(fileName, 0);
	}

	public static MuSession load(String fileName, int skip) {

		try (
				// TODO choose buffer size
				DataInputStream in = new DataInputStream(
						new BufferedInputStream(new FileInputStream(fileName), 128 * 1024));) {

			for (int i = 0; i < skip; i++) {
				in.mark(MuConstants.BLOCK_SIZE);
				MuHeader head = readHeader(in);
				in.reset();
				in.skipBytes(MuConstants.BLOCK_SIZE * head.getNtblk());
			}

			MuHeader head = readHeader(in);

			switch (head.getMobs().getEnumeratedMode()) {
			case RAW:
				MuRawData[] data = new MuRawData[head.getNdblk()];
				for (int i = 0; i < head.getNdblk(); i++) {
					data[i] = readRawData(in, head);
				}

				return new MuSession(head, data);
			// TODO Add other types of data
			default:
				return new MuSession(head, null);
			}

		} catch (EOFException end) {
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	private static MuHeader readHeader(DataInputStream in) throws Exception {

		in.mark(MuConstants.BLOCK_SIZE);

		MuHeader head = new MuHeader();

		head.setLnblk(in.readInt());
		head.setNtblk(in.readInt());
		head.setNdblk(in.readInt());
		head.setLnseg(in.readInt());
		head.setLnhead(in.readInt());
		head.setNhblk(in.readInt());

		byte[] programName = new byte[8];
		in.read(programName);
		head.setPrgnam(new String(programName));

		byte[] dateTime = new byte[24];
		in.read(dateTime);
		head.setLdtime(MuDateTime.toDateTime(dateTime));

		head.setNprog(in.readInt());

		in.read(dateTime);
		head.setRecsta(MuDateTime.toDateTimeWithFraction(dateTime));

		byte[] time = new byte[12];
		in.read(time);
		head.setRecend(MuTime.toTime(time));

		head.setIrec(in.readInt());
		head.setItrec(in.readInt());

		head.setMobs(new MuObservationMode(in.readInt()));
		head.setMhead1(new MuHeader1(in.readInt()));
		head.setMhead2(new MuHeader2(in.readInt()));
		head.setNdata(in.readInt());
		head.setNrdata(in.readInt());
		head.setNtseg(in.readInt());
		head.setNhigh(in.readInt());
		head.setNbeam(in.readInt());
		head.setNchan(in.readInt());
		head.setNccf(in.readInt());
		head.setIpp(in.readInt());
		head.setJstart(in.readInt());
		head.setJsint(in.readInt());
		head.setNcoh(in.readInt());
		head.setNicoh(in.readInt());

		head.setMtype(new MuDataType(in.readInt()));
		head.setMpulse(new MuPulse(in.readInt()));

		int[] macf = new int[21];
		for (int i = 0; i < 21; i++) {
			macf[i] = in.readInt();
		}
		head.setMacf(macf);

		int[] ibeam = new int[16];
		for (int i = 0; i < 16; i++) {
			ibeam[i] = in.readInt();
		}
		head.setIbeam(ibeam);

		head.setNfit(in.readInt());
		head.setLsubp(in.readInt());
		head.setNsubp(in.readInt());
		head.setMscan(in.readInt());

		byte[] hpnam = new byte[8];
		in.read(hpnam);
		head.setHpnam(new String(hpnam));

		head.setNomode(in.readInt());
		head.setNehead(in.readInt());

		int[] nicohm = new int[16];
		for (int i = 0; i < 16; i++) {
			nicohm[i] = in.readInt();
		}
		head.setNicohm(nicohm);

		head.setNsampl(in.readInt());

		byte[] reserv = new byte[MuConstants.RESERVED_BYTES];
		in.read(reserv);
		head.setReserv(reserv);

		byte[] oparam = new byte[16];
		in.read(oparam);
		head.setOparam(new String(oparam));

		head.setIprver(in.readInt());
		head.setIsta(in.readInt());
		head.setIstaus(in.readInt());
		head.setIend(in.readInt());
		head.setIendus(in.readInt());
		head.setNpseq(in.readInt());

		int[] itxcod = new int[MuConstants.MAX_PSEQ];
		for (int i = 0; i < MuConstants.MAX_PSEQ; i++) {
			itxcod[i] = in.readInt();
		}
		head.setItxcod(itxcod);

		head.setLdcdal(in.readInt());
		head.setNpsqal(in.readInt());

		int[] idcdal = new int[MuConstants.MAX_PSEQ];
		for (int i = 0; i < MuConstants.MAX_PSEQ; i++) {
			idcdal[i] = in.readInt();
		}
		head.setIdcdal(idcdal);

		head.setIsteer(in.readInt());

		int[] ibeam2 = new int[MuConstants.MAX_BEAM];
		for (int i = 0; i < MuConstants.MAX_BEAM; i++) {
			ibeam2[i] = in.readShort();
		}
		head.setIbeam2(ibeam2);

		head.setIbshap(in.readInt());
		head.setIazoff(in.readInt());
		head.setIzeoff(in.readInt());
		head.setIpolar(in.readInt());
		head.setNtxfrq(in.readInt());

		float[] txfreq = new float[MuConstants.MAX_NTXFRQ];
		for (int i = 0; i < MuConstants.MAX_NTXFRQ; i++) {
			txfreq[i] = in.readFloat();
		}
		head.setTxfreq(txfreq);

		head.setIgain(in.readInt());
		head.setItxatt(in.readInt());

		int[] irxatt = new int[MuConstants.MAX_ANCHAN];
		for (int i = 0; i < MuConstants.MAX_ANCHAN; i++) {
			irxatt[i] = in.readInt();
		}
		head.setIrxatt(irxatt);

		head.setItxon(in.readInt());

		int[] irxon = new int[MuConstants.MAX_ANCHAN];
		for (int i = 0; i < MuConstants.MAX_ANCHAN; i++) {
			irxon[i] = in.readInt();
		}
		head.setIrxon(irxon);

		int[] irxsel = new int[26];
		for (int i = 0; i < 26; i++) {
			irxsel[i] = in.readShort();
		}
		head.setIrxsel(irxsel);

		head.setIfiltr(in.readInt());
		head.setIrngzr(in.readInt());

		int[] istart = new int[MuConstants.MAX_BEAM];
		for (int i = 0; i < MuConstants.MAX_BEAM; i++) {
			istart[i] = in.readInt();
		}
		head.setIstart(istart);

		head.setIrxseq(in.readInt());

		int[] ichan = new int[MuConstants.MAX_CHAN];
		for (int i = 0; i < MuConstants.MAX_CHAN; i++) {
			ichan[i] = in.readInt();
		}
		head.setIchan(ichan);

		int[] ncoh2 = new int[MuConstants.MAX_CHAN];
		for (int i = 0; i < MuConstants.MAX_CHAN; i++) {
			ncoh2[i] = in.readInt();
		}
		head.setNcoh2(ncoh2);

		int[] nfft = new int[MuConstants.MAX_CHAN];
		for (int i = 0; i < MuConstants.MAX_CHAN; i++) {
			nfft[i] = in.readInt();
		}
		head.setNfft(nfft);

		int[] ndata2 = new int[MuConstants.MAX_CHAN];
		for (int i = 0; i < MuConstants.MAX_CHAN; i++) {
			ndata2[i] = in.readInt();
		}
		head.setNdata2(ndata2);

		int[][] ifft1 = new int[MuConstants.MAX_CHAN][2];
		for (int i = 0; i < MuConstants.MAX_CHAN; i++) {
			for (int j = 0; j < 2; j++) {
				ifft1[i][j] = in.readShort();
			}
		}
		head.setIfft1(ifft1);

		int[][] ifft2 = new int[MuConstants.MAX_CHAN][2];
		for (int i = 0; i < MuConstants.MAX_CHAN; i++) {
			for (int j = 0; j < 2; j++) {
				ifft2[i][j] = in.readShort();
			}
		}
		head.setIfft2(ifft2);

		int[][] ifft3 = new int[MuConstants.MAX_CHAN][2];
		for (int i = 0; i < MuConstants.MAX_CHAN; i++) {
			for (int j = 0; j < 2; j++) {
				ifft3[i][j] = in.readShort();
			}
		}
		head.setIfft3(ifft3);

		float[] rxfreq = new float[MuConstants.MAX_CHAN];
		for (int i = 0; i < MuConstants.MAX_CHAN; i++) {
			rxfreq[i] = in.readFloat();
		}
		head.setRxfreq(rxfreq);

		int itxfir[] = new int[MuConstants.MAX_FIR];
		for (int i = 0; i < MuConstants.MAX_FIR; i++) {
			itxfir[i] = in.readShort();
		}
		head.setItxfir(itxfir);

		int[][] igafir = new int[MuConstants.MAX_CHAN][2];
		for (int i = 0; i < MuConstants.MAX_CHAN; i++) {
			for (int j = 0; j < 2; j++) {
				igafir[i][j] = in.readShort();
			}
		}
		head.setIgafir(igafir);

		head.setIntptn(in.readInt());
		head.setIntrat(in.readInt());
		head.setNtxcic(in.readInt());
		head.setIgacic(in.readInt());

		int[] nrxcic = new int[MuConstants.MAX_CHAN];
		for (int i = 0; i < MuConstants.MAX_CHAN; i++) {
			nrxcic[i] = in.readInt();
		}
		head.setNrxcic(nrxcic);

		int[] icrrat = new int[MuConstants.MAX_CHAN];
		for (int i = 0; i < MuConstants.MAX_CHAN; i++) {
			icrrat[i] = in.readInt();
		}
		head.setIcrrat(icrrat);

		head.setSealvl(in.readFloat());
		head.setIheadf(in.readInt());

		byte[] coment = new byte[80];
		in.read(coment);
		head.setComent(new String(coment));

		head.setNfftc(in.readInt());
		head.setNbls(in.readInt());
		head.setIchanc(in.readInt());

		byte[] usrhdr = new byte[208];
		in.read(usrhdr);
		head.setUsrhdr(usrhdr);

		in.reset();
		in.skipBytes(MuConstants.BLOCK_SIZE);

		return head;
	}

	private static MuRawData readRawData(DataInputStream in, MuHeader head) throws Exception {

		in.mark(MuConstants.BLOCK_SIZE);

		int beam = in.readByte();
		int channel = in.readByte();
		int height = in.readShort();

		double[] real = new double[head.getNdata()];
		for (int i = 0; i < real.length; i++) {
			real[i] = in.readFloat();
		}

		double[] imagine = new double[head.getNdata()];
		for (int i = 0; i < imagine.length; i++) {
			imagine[i] = in.readFloat();
		}

		MuRawData data = new MuRawData(new MuEachHeader(beam, channel, height), real, imagine);

		in.reset();
		in.skipBytes(MuConstants.BLOCK_SIZE);

		return data;
	}

}
