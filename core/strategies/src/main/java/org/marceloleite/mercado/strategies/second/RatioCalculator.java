package org.marceloleite.mercado.strategies.second;

import java.math.BigDecimal;

public class RatioCalculator {

	public BigDecimal calculate(BigDecimal firstValue, BigDecimal secondValue) {
		BigDecimal result;
		if ( firstValue.compareTo(BigDecimal.ZERO) == 0 ) {
			if ( secondValue.compareTo(BigDecimal.ZERO) == 0) {
				result = new BigDecimal("1.0");
			} else {
				result = new BigDecimal("0.0");
			}
		} else {
			if ( secondValue.compareTo(BigDecimal.ZERO) == 0 ) {
				/* TODO: Create a constant for positive infinity. */
				result = new BigDecimal("10E20");
			} else {
				result = firstValue.divide(secondValue); 
			}
		}
		return result;
	}
}
