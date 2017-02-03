package com.albom.iion.isr.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import com.albom.utils.DataBuffer;

public abstract class SNewFileFS {

	public static SNewFile loadFile(String fileName) {
		SNewFile file = new SNewFile();
		loadFile(file, fileName);
		return file;
	}
	
	public static void loadFile(SNewFile file, String fileName) {

		byte[] buffer = null;

		try {
			buffer = readAllFile(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		file.setTime(LocalDateTime.of(DataBuffer.getWordBigEndian(buffer, 4), DataBuffer.getWordBigEndian(buffer, 2),
				DataBuffer.getWordBigEndian(buffer, 0), DataBuffer.getWordBigEndian(buffer, 6),
				DataBuffer.getWordBigEndian(buffer, 8), DataBuffer.getWordBigEndian(buffer, 10)));

		file.setSession(DataBuffer.getWordBigEndian(buffer, 12));
		file.setIntegration(DataBuffer.getWordBigEndian(buffer, 14));

		int offset = 20;
		file.setCorAA(read2D(buffer, offset));
		file.setCorAB(read2D(buffer, offset += SNewFile.N_ALT * SNewFile.N_LAG * 4));
		file.setCorBB(read2D(buffer, offset += SNewFile.N_ALT * SNewFile.N_LAG * 4));
		file.setCorBA(read2D(buffer, offset += SNewFile.N_ALT * SNewFile.N_LAG * 4));

		file.setBiasA(read1D(buffer, offset += SNewFile.N_ALT * SNewFile.N_LAG * 4));
		file.setBiasB(read1D(buffer, offset += SNewFile.N_ALT * 4));

		file.setPowerShort1(read1D(buffer, offset += SNewFile.N_ALT * 4));
		file.setPowerShort2(read1D(buffer, offset += SNewFile.N_ALT * 4));

		file.setBiasShort1(read1D(buffer, offset += SNewFile.N_ALT * 4));
		file.setBiasShort2(read1D(buffer, offset += SNewFile.N_ALT * 4));

	}

	public static void saveFile(SNewFile file, String fileName) {
		byte[] buffer = new byte[223060];
		DataBuffer.setWordBigEndian(buffer, 0, file.getTime().getDayOfMonth());
		DataBuffer.setWordBigEndian(buffer, 2, file.getTime().getMonthValue());
		DataBuffer.setWordBigEndian(buffer, 4, file.getTime().getYear());
		DataBuffer.setWordBigEndian(buffer, 6, file.getTime().getHour());
		DataBuffer.setWordBigEndian(buffer, 8, file.getTime().getMinute());
		DataBuffer.setWordBigEndian(buffer, 10, file.getTime().getSecond());
		DataBuffer.setWordBigEndian(buffer, 12, file.getSession());
		DataBuffer.setWordBigEndian(buffer, 14, file.getIntegration());
		DataBuffer.setWordBigEndian(buffer, 16, 0);
		DataBuffer.setWordBigEndian(buffer, 18, 0);

		int offset = 20;
		offset = write2D(buffer, offset, file.getCorAA());
		offset = write2D(buffer, offset, file.getCorAB());
		offset = write2D(buffer, offset, file.getCorBB());
		offset = write2D(buffer, offset, file.getCorBA());
		
		offset = write1D(buffer, offset, file.getBiasA());
		offset = write1D(buffer, offset, file.getBiasB());
		
		offset = write1D(buffer, offset, file.getPowerShort1());
		offset = write1D(buffer, offset, file.getPowerShort2());
		
		offset = write1D(buffer, offset, file.getBiasShort1());
		offset = write1D(buffer, offset, file.getBiasShort2());
		
		try {
			writeAllFile(fileName, buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static byte[] readAllFile(String fileName) throws IOException {
		Path path = Paths.get(fileName);
		return Files.readAllBytes(path);
	}

	private static void writeAllFile(String fileName, byte buffer[]) throws IOException {
		Path path = Paths.get(fileName);
		Files.write(path, buffer);
	}
	
	private static int[][] read2D(byte[] buffer, int offset) {
		int[][] result = new int[SNewFile.N_ALT][SNewFile.N_LAG];
		for (int h = 0; h < SNewFile.N_ALT; h++) {
			for (int tau = 0; tau < SNewFile.N_LAG; tau++) {
				result[h][tau] = DataBuffer.getDoubleWordBigEndian(buffer, offset);
				offset += 4;
			}
		}
		return result;
	}

	private static int write2D(byte[] buffer, int offset, int data[][]) {
		for (int h = 0; h < SNewFile.N_ALT; h++) {
			for (int tau = 0; tau < SNewFile.N_LAG; tau++) {
				DataBuffer.setDoubleWordBigEndian(buffer, offset, data[h][tau]);
				offset += 4;
			}
		}
		return offset;
	}

	private static int write1D(byte[] buffer, int offset, int data[]) {
		for (int h = 0; h < SNewFile.N_ALT; h++) {
			DataBuffer.setDoubleWordBigEndian(buffer, offset, data[h]);
			offset += 4;
		}
		return offset;
	}

	private static int[] read1D(byte[] buffer, int offset) {
		int[] result = new int[SNewFile.N_ALT];
		for (int h = 0; h < SNewFile.N_ALT; h++) {
			result[h] = DataBuffer.getDoubleWordBigEndian(buffer, offset);
			offset += 4;
		}
		return result;
	}

}
