package org.marceloleite.mercado.commons;

import java.util.List;

import org.marceloleite.mercado.commons.formatter.NonDigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.formatter.PercentageFormatter;

public class Statistics {

	private Double base;

	private CircularArray<Double> circularArray;

	private double summation;

	private double derivativeLastsSummation;

	private double variation;

	private double average;

	private double next;

	private double ratio;

	private int nextValueSteps;

	public Statistics(int circularArraySize, int nextValueSteps) {
		super();
		this.circularArray = new CircularArray<>(circularArraySize);
		this.nextValueSteps = nextValueSteps;
		this.summation = 0.0;
		this.derivativeLastsSummation = 0.0;
		this.base = null;
	}

	public void setBase(double base) {
		this.base = base;
	}

	public double getBase() {
		return base;
	}

	public double getVariation() {
		return variation;
	}

	public double getAverage() {
		return average;
	}

	public double getNext() {
		return next;
	}

	public double getRatio() {
		return ratio;
	}

	public void add(double value) {
		if (!circularArray.isFilled()) {
			circularArray.add(value);
			calculateValues();
		} else {
			calculatePreAddValues();
			circularArray.add(value);
			calculatePosAddValues();
		}
	}

	private double calculateNext() {
		if (Double.isFinite(variation)) {
			return average + (variation * nextValueSteps);
		} else {
			return Double.NaN;
		}
	}

	private void calculatePreAddValues() {
		if (circularArray.isFilled()) {
			double firstLastPrice = circularArray.get(0);
			double secondLastPrice = circularArray.get(1);

			double subtract = secondLastPrice - firstLastPrice;

			derivativeLastsSummation -= subtract;
			summation -= firstLastPrice;
		}
	}

	private void calculatePosAddValues() {
		double currentLastPrice = circularArray.get(circularArray.getOccupiedPositions() - 1);

		int previousPosition = 0;
		if (circularArray.getOccupiedPositions() <= 1) {
			previousPosition = 0;
		} else {
			previousPosition = circularArray.getOccupiedPositions() - 2;
		}
		double previousLastPrice = circularArray.get(previousPosition);

		double subtract = currentLastPrice - previousLastPrice;

		derivativeLastsSummation += subtract;
		summation += currentLastPrice;
		average = calculateAverage();
		variation = calculateVariation();
		next = calculateNext();
		ratio = calculateRatio();
	}

	private void calculateValues() {
		List<Double> list = circularArray.asList();
		double previousLast;
		summation = 0.0;
		derivativeLastsSummation = 0.0;
		for (int counter = 0; counter < list.size(); counter++) {
			if (counter == 0) {
				previousLast = list.get(counter);
			} else {
				previousLast = list.get(counter - 1);
			}
			
			double currentLast = list.get(counter);
			double subtract = currentLast - previousLast;

			derivativeLastsSummation += subtract;
			summation += currentLast;
		}
		average = calculateAverage();
		variation = calculateVariation();
		next = calculateNext();
		ratio = calculateRatio();
	}

	private double calculateVariation() {
		if (circularArray.getOccupiedPositions() == 0) {
			return Double.NaN;
		} else {
			return derivativeLastsSummation / circularArray.getOccupiedPositions();
		}
	}

	private Double calculateAverage() {
		if (circularArray.getOccupiedPositions() == 0) {
			return Double.NaN;
		} else {
			return summation / circularArray.getOccupiedPositions();
		}
	}

	private double calculateRatio() {
		if (!Double.isFinite(next)) {
			return Double.NaN;
		} else {
			double first;
			if (base != null) {
				first = base;
			} else {
				first = average;
			}
			return VariationCalculator.getInstance()
					.calculate(average, first);
		}
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[ratio: " + PercentageFormatter.getInstance()
				.format(ratio));
		stringBuilder.append(", base: " + NonDigitalCurrencyFormatter.getInstance()
				.format(base));
		stringBuilder.append(", average: " + NonDigitalCurrencyFormatter.getInstance()
				.format(average));
		stringBuilder.append(", variation: " + NonDigitalCurrencyFormatter.getInstance()
				.format(variation));
		stringBuilder.append(", next: " + NonDigitalCurrencyFormatter.getInstance()
				.format(next) + "]");
		return stringBuilder.toString();
	}

	public CircularArray<Double> getCircularArray() {
		return circularArray;
	}
}
