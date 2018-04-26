package org.marceloleite.mercado.strategies.second;

import org.marceloleite.mercado.commons.MercadoBigDecimal;

public class RatioCalculator {
	
	private static RatioCalculator instance;
	
	private RatioCalculator() {}

	public MercadoBigDecimal calculate(double firstValue, double secondValue) {
		MercadoBigDecimal result;
		if ( firstValue.compareTo(MercadoBigDecimal.ZERO) == 0 ) {
			if ( secondValue.compareTo(MercadoBigDecimal.ZERO) == 0) {
				result = new MercadoBigDecimal("1.0");
			} else {
				result = new MercadoBigDecimal("0.0");
			}
		} else {
			if ( secondValue.compareTo(MercadoBigDecimal.ZERO) == 0 ) {
				/* TODO: Create a constant for positive infinity. */
				result = MercadoBigDecimal.POSITIVE_INFINITY;
			} else {
				result = firstValue.divide(secondValue); 
			}
		}
		return result;
	}
	
	public static RatioCalculator getInstance() {
		if (instance == null) {
			instance = new RatioCalculator();
		}
		return instance;
	}
}
