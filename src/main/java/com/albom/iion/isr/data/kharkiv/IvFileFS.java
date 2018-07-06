package com.albom.iion.isr.data.kharkiv;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.albom.util.DataBuffer;

public abstract class IvFileFS {

	public static boolean test(Path file) {
		try (InputStream inputStream = new FileInputStream(file.toString())) {
			byte[] header = new byte[4];
			inputStream.read(header);
			if (DataBuffer.getDoubleWordLittleEndian(header, 0) != IvFile.VERSION) {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static IvFile load(Path file) {

		IvFile iv = new IvFile();

		try (InputStream inputStream = new FileInputStream(file.toString())) {
			byte[] temp = new byte[4];
			inputStream.read(temp);
			int version = DataBuffer.getDoubleWordLittleEndian(temp, 0);
			temp = new byte[20];
			inputStream.read(temp);
			LocalDateTime date = LocalDateTime.parse(new String(temp, "US-ASCII"),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss\0"));
			temp = new byte[4];
			inputStream.read(temp);
			int nScan = DataBuffer.getDoubleWordLittleEndian(temp, 0);
			inputStream.read(temp);
			int nPoint = DataBuffer.getDoubleWordLittleEndian(temp, 0);
			inputStream.read(temp);
			int interval = DataBuffer.getDoubleWordLittleEndian(temp, 0);
			temp = new byte[44];
			inputStream.read(temp);

			List<IvScan> scans = new ArrayList<>();
			for (int i = 0; i < nScan * 7; i++) {
				IvScan scan = new IvScan();
				temp = new byte[1];
				inputStream.read(temp);
				int type = temp[0];
				scan.setType(type);
				int[] re = new int[nPoint];
				int[] im = new int[nPoint];
				temp = new byte[nPoint * 4];
				inputStream.read(temp);
				int offset = 0;
				for (int j = 0; j < nPoint; j++) {
					re[j] = DataBuffer.getWordLittleEndian(temp, offset);
					im[j] = DataBuffer.getWordLittleEndian(temp, offset + nPoint * 2);
					offset += 2;
				}
				scan.setRe(re);
				scan.setIm(im);

				scans.add(scan);

			}

			iv.setVersion(version);
			iv.setDate(date);
			iv.setNumPoint(nPoint);
			iv.setNumScan(nScan);
			iv.setInterval(interval);

			iv.setScans(scans);

		} catch (IOException e) {
			return null;
		}
		return iv;
	}

}
