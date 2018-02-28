package org.marceloleite.mercado.simulator.strategies.second;

public class RatioCalculator {

	public double calculate(double firstValue, double secondValue) {
		double result;
		if ( firstValue == 0l ) {
			if ( secondValue == 0l) {
				result = 1.0;
			} else {
				result = 0.0;
			}
		} else {
			if ( secondValue == 0l ) {
				result = Double.POSITIVE_INFINITY;
			} else {
				result = (double)firstValue/(double)secondValue; 
			}
		}
		return result;
	}
}
