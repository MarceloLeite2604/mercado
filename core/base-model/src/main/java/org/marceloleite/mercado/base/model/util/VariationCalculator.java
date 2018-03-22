package org.marceloleite.mercado.base.model.util;

import java.math.BigDecimal;

public class VariationCalculator {

	public BigDecimal calculate(BigDecimal  firstValue, BigDecimal  secondValue) {
		if (secondValue.compareTo(BigDecimal.ZERO) == 0) {
			/* TODO: Representing a positive infinity (?) */
			return new BigDecimal("10E20") ;
		} else {
			return (firstValue.divide(secondValue).subtract(BigDecimal.ONE));
		}
	}
}
