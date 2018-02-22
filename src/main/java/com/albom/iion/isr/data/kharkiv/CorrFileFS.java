package com.albom.iion.isr.data.kharkiv;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import com.albom.utils.DataBuffer;

public abstract class CorrFileFS {

	public static boolean load(CorrFile file, String fileName) {

		try {
			InputStream input = new FileInputStream(fileName);
			byte[] buffer = new byte[20];
			input.read(buffer);
			file.setTime(
					LocalDateTime.of(DataBuffer.getWordBigEndian(buffer, 4), DataBuffer.getWordBigEndian(buffer, 2),
							DataBuffer.getWordBigEndian(buffer, 0), DataBuffer.getWordBigEndian(buffer, 6),
							DataBuffer.getWordBigEndian(buffer, 8), DataBuffer.getWordBigEndian(buffer, 10)));

			file.setSession(DataBuffer.getWordBigEndian(buffer, 12));
			file.setIntegration(DataBuffer.getWordBigEndian(buffer, 14));

			input.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static boolean load(SNewFile file, String fileName) {
		if (SNewFileFS.test(fileName)) {
			return load((CorrFile) file, fileName);
		} else {
			return false;
		}
	}

}
