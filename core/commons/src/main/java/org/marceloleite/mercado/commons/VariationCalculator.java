package org.marceloleite.mercado.commons;

import java.math.BigDecimal;

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
	
	public double calculate(BigDecimal firstValue, BigDecimal secondValue) {
		return calculate(firstValue.doubleValue(), secondValue.doubleValue());
	}
	
	public static VariationCalculator getInstance() {
		if (instance == null) {
			instance = new VariationCalculator();
		}
		return instance;
	}
}
