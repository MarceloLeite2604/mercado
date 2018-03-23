package org.marceloleite.mercado.base.model.util;

import org.marceloleite.mercado.commons.MercadoBigDecimal;

public class VariationCalculator {

	public MercadoBigDecimal calculate(MercadoBigDecimal  firstValue, MercadoBigDecimal  secondValue) {
		if (secondValue.compareTo(MercadoBigDecimal.ZERO) == 0) {
			return MercadoBigDecimal.POSITIVE_INFINITY;
		} else {
			return new MercadoBigDecimal(firstValue.divide(secondValue).subtract(MercadoBigDecimal.ONE));
		}
	}
}
