package org.marceloleite.mercado.commons.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

public class ByteUtils {
	
	private static final int HEXADECIMAL_RADIX = 16;

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
		if (string == null ) {
			throw new IllegalArgumentException("String to convert is null.");
		}
		
		if ( string.length()%2 != 0) {
			throw new IllegalArgumentException("Invalid string size to convert ("+string.length()+").");
		}
		
		string = string.toUpperCase();
		if ( !string.matches("[0-9A-F]*") ) {
			throw new IllegalArgumentException("Invalid string content to convert.");
		}
		
		return DatatypeConverter.parseHexBinary(string);
		
	}

	private static String[] splitByteString(String string) {
		Pattern pattern = Pattern.compile(".{2}");
		Matcher matcher = pattern.matcher(string);
		
		List<String> stringList = new ArrayList<>();
		while (matcher.find()) {
			stringList.add(string.substring(matcher.start(), matcher.end()));
		}
		String[] stringArray = new String[stringList.size()];
		return stringList.toArray(stringArray);
	}
}
