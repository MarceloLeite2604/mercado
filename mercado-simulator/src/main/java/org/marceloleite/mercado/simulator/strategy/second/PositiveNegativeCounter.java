package org.marceloleite.mercado.simulator.strategy.second;

import java.util.List;

public class PositiveNegativeCounter {
	
	public long countPositives(List<Double> values) {
		return values.stream().filter(value -> value > 0).count();
	}
	
	public long countNegatives(List<Double> values) {
		return values.stream().filter(value -> value < 0).count();
	}
}
