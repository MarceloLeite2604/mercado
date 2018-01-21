package org.marceloleite.mercado.simulator.strategy.second;

public class VariationCalculator {

	public Double calculate(double firstValue, double secondValue) {
		if (secondValue == 0) {
			return Double.POSITIVE_INFINITY;
		} else {
			return (firstValue / secondValue) - 1.0;
		}
	}
}
