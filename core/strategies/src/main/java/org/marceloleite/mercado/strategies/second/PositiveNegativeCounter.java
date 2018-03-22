package org.marceloleite.mercado.strategies.second;

import java.math.BigDecimal;
import java.util.List;

public class PositiveNegativeCounter {
	
	public long countPositives(List<BigDecimal> values) {
		return values.stream().filter(value -> value.compareTo(BigDecimal.ZERO) > 0).count();
	}
	
	public long countNegatives(List<BigDecimal> values) {
		return values.stream().filter(value -> value.compareTo(BigDecimal.ZERO) < 0).count();
	}
}
