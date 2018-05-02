package org.marceloleite.mercado.commons.utils;

import javax.xml.bind.DatatypeConverter;

public final class ByteUtils {

	private ByteUtils() {
	}

	public static String toString(byte[] bytes) {
		StringBuilder stringBuilder = new StringBuilder();
		for (byte b : bytes) {
			stringBuilder.append(toString(b));
		}
		return stringBuilder.toString();
	}

	public static String toString(byte b) {
		return String.format("%02x", b);
	}

	public static byte[] toBytes(String string) {
		if (string == null) {
			throw new IllegalArgumentException("String to convert is null.");
		}

		if (string.length() % 2 != 0) {
			throw new IllegalArgumentException("Invalid string size to convert (" + string.length() + ").");
		}

		string = string.toUpperCase();
		if (!string.matches("[0-9A-F]*")) {
			throw new IllegalArgumentException("Invalid string content to convert.");
		}

		return DatatypeConverter.parseHexBinary(string);
	}
}
