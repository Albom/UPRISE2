package com.albom.utils;


/**
 * 
 * @author Albom
 */

abstract public class DataBuffer {

	/**
	 * get word (2 bytes) from buffer
	 * 
	 * @param buffer
	 *            byte buffer
	 * @param offset
	 *            offset
	 * @return
	 */
	public static int getWordBigEndian(byte[] buffer, int offset) {
		int b0, b1; 
		b0 = buffer[offset + 1] >= 0 ? buffer[offset + 1]
				: 256 + buffer[offset + 1];
		b1 = buffer[offset] >= 0 ? buffer[offset] : 255 - buffer[offset];
		return b0 + b1 * 256;
	}

	/**
	 * get double word (4 bytes) from buffer
	 * 
	 * @param buffer
	 *            byte buffer
	 * @param offset
	 *            offset
	 * @return
	 */
	public static int getDoubleWordBigEndian(byte[] buffer, int offset) {
		int b0, b1, b2, b3; 
		b0 = buffer[offset + 3] >= 0 ? buffer[offset + 3]
				: 256 + buffer[offset + 3];
		b1 = buffer[offset + 2] >= 0 ? buffer[offset + 2]
				: 256 + buffer[offset + 2];
		b2 = buffer[offset + 1] >= 0 ? buffer[offset + 1]
				: 256 + buffer[offset + 1];
		b3 = buffer[offset] >= 0 ? buffer[offset] : 256 + buffer[offset];
		return b0 + b1 * 256 + b2 * 256 * 256 + b3 * 256 * 256 * 256;
	}

	/**
	 * get double word (4 bytes) from buffer
	 * 
	 * @param buffer
	 *            byte buffer
	 * @param offset
	 *            offset
	 * @return
	 */
	public static int getDoubleWordLittleEndian(byte[] buffer, int offset) {
		int b0, b1, b2, b3; 
		b0 = buffer[offset] >= 0 ? buffer[offset]
				: 256 + buffer[offset];
		b1 = buffer[offset + 1] >= 0 ? buffer[offset + 1]
				: 256 + buffer[offset + 1];
		b2 = buffer[offset + 2] >= 0 ? buffer[offset + 2]
				: 256 + buffer[offset + 2];
		b3 = buffer[offset + 3] >= 0 ? buffer[offset + 3] 
				: 256 + buffer[offset + 3];
		return b0 + b1 * 256 + b2 * 256 * 256 + b3 * 256 * 256 * 256;
	}	
	
	/**
	 * set word (2 bytes) to buffer
	 * 
	 * @param buffer
	 *            byte buffer
	 * @param offset
	 *            offset
	 * @param value
	 *            word value
	 * @return
	 */
	public static void setWordBigEndian(byte[] buffer, int offset, int value) {
		buffer[offset] =  (byte) ((value & 0xff00) >> 8);
		buffer[offset + 1] = (byte) (value & 0xff);
	}

	
	/**
	 * set double word (4 bytes) to buffer
	 * 
	 * @param buffer
	 *            byte buffer
	 * @param offset
	 *            offset
	 * @param value
	 *            double word value
	 * @return
	 */	
	public static void setDoubleWordBigEndian(byte[] buffer, int offset, int value) {
		buffer[offset] = (byte) ((value & 0xff000000) >> 24);
		buffer[offset + 1] = (byte) ((value & 0x00ff0000) >> 16);
		buffer[offset + 2] = (byte) ((value & 0x0000ff00) >> 8);
		buffer[offset + 3] = (byte) (value & 0x000000ff);
	}	

	/**
	 * set double word (4 bytes) to buffer
	 * 
	 * @param buffer
	 *            byte buffer
	 * @param offset
	 *            offset
	 * @param value
	 *            double word value
	 * @return
	 */	
	public static void setDoubleWordLittleEndian(byte[] buffer, int offset, int value) {
		buffer[offset + 3] = (byte) ((value & 0xff000000) >> 24);
		buffer[offset + 2] = (byte) ((value & 0x00ff0000) >> 16);
		buffer[offset + 1] = (byte) ((value & 0x0000ff00) >> 8);
		buffer[offset + 0] = (byte) (value & 0x000000ff);
	}	
	
}
