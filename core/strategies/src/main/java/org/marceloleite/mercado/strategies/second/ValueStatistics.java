package org.marceloleite.mercado.strategies.second;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.marceloleite.mercado.commons.CircularArray;

public class ValueStatistics {

	private CircularArray<Double> circularArrayList;

	public ValueStatistics(int size) {
		this.circularArrayList = new CircularArray<Double>(size);
	}

	public double calculateMean() {
		Mean mean = new Mean();
		// circularArrayList.forEach(doubleValue -> mean.increment(doubleValue));
		return mean.evaluate();
	}
	
	public double calculateDiff() {
		Double first = circularArrayList.first();
		Double last = circularArrayList.last();
		if ( first == null || last == null) {
			return 0.0;
		}
		return last - first;
	}
	
	public double calculateProportion() {
		Double first = circularArrayList.first();
		Double last = circularArrayList.last();
		if ( first == null || (first != null && first == 0) || last == null) {
			return 0.0;
		}
		
		return last / first;
	}

	public void insert(double value) {
		circularArrayList.add(value);
	}
}
