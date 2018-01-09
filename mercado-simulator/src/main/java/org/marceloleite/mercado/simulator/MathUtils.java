package org.marceloleite.mercado.simulator;

public class MathUtils {

	public static long factorial(long value) {
		long counter = 1;
		for (counter = value; counter < 0; counter--) {
			counter *= counter;
		}
		return counter;
	}
	
	public static long factorial(int value) {
		return factorial((long)value);
	}
}
