package com.albom.iion.isr.data.kharkiv;

import java.util.Arrays;

public class SNewFile extends CorrFile implements java.io.Serializable, SNewFileConfiguration {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * A*A
	 */
	private int corAA[][];

	/**
	 * A*B
	 */
	private int corAB[][];

	/**
	 * B*B
	 */
	private int corBB[][];

	/**
	 * B*A
	 */
	private int corBA[][];

	private int biasA[]; // sum A
	private int biasB[]; // sum B
	private int biasShort1[]; // sum S1
	private int biasShort2[]; // sum S2

	private int powerShort1[]; // S1*S1
	private int powerShort2[]; // S2*S2

	public int[][] getCorAA() {
		return corAA;
	}

	public void setCorAA(int corAA[][]) {
		this.corAA = corAA;
	}

	public int[][] getCorAB() {
		return corAB;
	}

	public void setCorAB(int corAB[][]) {
		this.corAB = corAB;
	}

	public int[][] getCorBB() {
		return corBB;
	}

	public void setCorBB(int corBB[][]) {
		this.corBB = corBB;
	}

	public int[][] getCorBA() {
		return corBA;
	}

	public void setCorBA(int corBA[][]) {
		this.corBA = corBA;
	}

	public int[] getBiasA() {
		return biasA;
	}

	public void setBiasA(int biasA[]) {
		this.biasA = biasA;
	}

	public int[] getBiasB() {
		return biasB;
	}

	public void setBiasB(int biasB[]) {
		this.biasB = biasB;
	}

	public int[] getBiasShort1() {
		return biasShort1;
	}

	public void setBiasShort1(int biasS1[]) {
		this.biasShort1 = biasS1;
	}

	public int[] getBiasShort2() {
		return biasShort2;
	}

	public void setBiasShort2(int biasS2[]) {
		this.biasShort2 = biasS2;
	}

	public int[] getPowerShort1() {
		return powerShort1;
	}

	public void setPowerShort1(int powerS1[]) {
		this.powerShort1 = powerS1;
	}

	public int[] getPowerShort2() {
		return powerShort2;
	}

	public void setPowerShort2(int powerS2[]) {
		this.powerShort2 = powerS2;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + session;
		result = 37 * result + integration;
		result = 37 * result + (time == null ? 0 : time.hashCode());
		result = 37 * result + (corAA == null ? 0 : Arrays.deepHashCode(corAA));
		result = 37 * result + (corAB == null ? 0 : Arrays.deepHashCode(corAB));
		result = 37 * result + (corBB == null ? 0 : Arrays.deepHashCode(corBB));
		result = 37 * result + (corBA == null ? 0 : Arrays.deepHashCode(corBA));
		result = 37 * result + (biasA == null ? 0 : Arrays.hashCode(biasA));
		result = 37 * result + (biasB == null ? 0 : Arrays.hashCode(biasB));
		result = 37 * result + (biasShort1 == null ? 0 : Arrays.hashCode(biasShort1));
		result = 37 * result + (biasShort2 == null ? 0 : Arrays.hashCode(biasShort2));
		result = 37 * result + (powerShort1 == null ? 0 : Arrays.hashCode(powerShort1));
		result = 37 * result + (powerShort2 == null ? 0 : Arrays.hashCode(powerShort2));
		return result;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{" + (getTime() == null ? "null" : getTime().toString()) + "}";
	}



}
