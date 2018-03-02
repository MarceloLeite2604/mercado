package org.marceloleite.mercado.commons.util;

public class MathUtils {

	public static long factorial(long value) {
		long total = 1;
		for (long counter = value; counter > 0; counter--) {
			total *= counter;
		}
		return total;
	}
	
	public static long factorial(int value) {
		return factorial((long)value);
	}
}
