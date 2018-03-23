package org.marceloleite.mercado.strategies.second;

import java.util.List;

import org.marceloleite.mercado.commons.MercadoBigDecimal;

public class PositiveNegativeCounter {
	
	public long countPositives(List<MercadoBigDecimal> values) {
		return values.stream().filter(value -> value.compareTo(MercadoBigDecimal.ZERO) > 0).count();
	}
	
	public long countNegatives(List<MercadoBigDecimal> values) {
		return values.stream().filter(value -> value.compareTo(MercadoBigDecimal.ZERO) < 0).count();
	}
}
