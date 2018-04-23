package org.marceloleite.mercado.commons;

public class VariationCalculator {
	
	private static VariationCalculator instance;
	
	private VariationCalculator() {
	}

	public double calculate(double firstValue, double secondValue) {
		if (secondValue == 0.0) {
			return Double.POSITIVE_INFINITY;
		} else {
			return firstValue/secondValue;
		}
	}
	
	public static VariationCalculator getInstance() {
		if (instance == null) {
			instance = new VariationCalculator();
		}
		return instance;
	}
}
