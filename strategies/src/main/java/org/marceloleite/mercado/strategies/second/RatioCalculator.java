package org.marceloleite.mercado.strategies.second;

public class RatioCalculator {
	
	private static RatioCalculator instance;
	
	private RatioCalculator() {}

	public Double calculate(double firstValue, double secondValue) {
		double result;
		if ( firstValue  == 0 ) {
			if ( secondValue  == 0) {
				result = 0.0;
			} else {
				result = 0.0;
			}
		} else {
			if ( secondValue  == 0 ) {
				result = Double.POSITIVE_INFINITY;
			} else {
				result = firstValue/secondValue; 
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
