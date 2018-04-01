package org.marceloleite.mercado.commons;

public class VariationCalculator {

	public MercadoBigDecimal calculate(MercadoBigDecimal  firstValue, MercadoBigDecimal  secondValue) {
		if (secondValue.compareTo(MercadoBigDecimal.ZERO) == 0) {
			return MercadoBigDecimal.POSITIVE_INFINITY;
		} else {
			return new MercadoBigDecimal(firstValue.divide(secondValue).subtract(MercadoBigDecimal.ONE));
		}
	}
}
