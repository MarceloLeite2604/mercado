package org.marceloleite.mercado.commons;

import java.util.List;

import org.marceloleite.mercado.commons.formatter.NonDigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.formatter.PercentageFormatter;

public class Statistics {

	private MercadoBigDecimal base;

	private CircularArray<MercadoBigDecimal> circularArray;
	
	private MercadoBigDecimal summation;

	private MercadoBigDecimal derivativeLastsSummation;

	private MercadoBigDecimal variation;

	private MercadoBigDecimal average;

	private MercadoBigDecimal next;

	private MercadoBigDecimal ratio;
	
	private MercadoBigDecimal nextValueSteps;

	public Statistics(int circularArraySize, int nextValueSteps) {
		super();
		this.circularArray = new CircularArray<>(circularArraySize);
		this.nextValueSteps = new MercadoBigDecimal(nextValueSteps);
		this.summation = new MercadoBigDecimal("0");
		this.derivativeLastsSummation = new MercadoBigDecimal("0");
	}

	public void setBase(MercadoBigDecimal base) {
		this.base = base;
	}

	public MercadoBigDecimal getBase() {
		return base;
	}

	public MercadoBigDecimal getVariation() {
		return variation;
	}

	public MercadoBigDecimal getAverage() {
		return average;
	}

	public MercadoBigDecimal getNext() {
		return next;
	}
	
	public MercadoBigDecimal getRatio() {
		return ratio;
	}

	public void add(MercadoBigDecimal value) {
		if (!circularArray.isFilled()) {
			circularArray.add(value);
			calculateValues();
		} else {
			calculatePreAddValues();
			circularArray.add(value);
			calculatePosAddValues();
		}
	}

	private MercadoBigDecimal calculateNext() {
		if (variation.equals(MercadoBigDecimal.NOT_A_NUMBER)) {
			return new MercadoBigDecimal(MercadoBigDecimal.NOT_A_NUMBER);
		} else {
			return average.add(variation.multiply(nextValueSteps));
		}
	}

	private void calculatePreAddValues() {
		if (circularArray.isFilled()) {
			MercadoBigDecimal firstLastPrice = circularArray.get(0);
			MercadoBigDecimal secondLastPrice = circularArray.get(1);

			MercadoBigDecimal subtract = secondLastPrice.subtract(firstLastPrice);

			derivativeLastsSummation = derivativeLastsSummation.subtract(subtract);
			summation = summation.subtract(firstLastPrice);
		}
	}

	private void calculatePosAddValues() {
		MercadoBigDecimal currentLastPrice = circularArray
				.get(circularArray.getOccupiedPositions() - 1);

		int previousPosition = 0;
		if (circularArray.getOccupiedPositions() <= 1) {
			previousPosition = 0;
		} else {
			previousPosition = circularArray.getOccupiedPositions() - 2;
		}
		MercadoBigDecimal previousLastPrice = circularArray.get(previousPosition);

		MercadoBigDecimal subtract = currentLastPrice.subtract(previousLastPrice);

		derivativeLastsSummation = derivativeLastsSummation.add(subtract);
		summation = summation.add(currentLastPrice);
		average = calculateAverage();
		variation = calculateVariation();
		next = calculateNext();
		ratio = calculateRatio();
	}

	private void calculateValues() {
		List<MercadoBigDecimal> list = circularArray.asList();
		MercadoBigDecimal previousLast = null;
		summation = new MercadoBigDecimal("0");
		derivativeLastsSummation = new MercadoBigDecimal("0");
		for (int counter = 0; counter < list.size(); counter++) {
			if (counter == 0) {
				previousLast = list.get(counter);
			} else {
				previousLast = list.get(counter - 1);
			}
			MercadoBigDecimal currentLast = list.get(counter);

			MercadoBigDecimal subtract = currentLast.subtract(previousLast);

			derivativeLastsSummation = derivativeLastsSummation.add(subtract);
			summation = summation.add(currentLast);
		}
		average = calculateAverage();
		variation = calculateVariation();
		next = calculateNext();
		ratio = calculateRatio();
	}

	private MercadoBigDecimal calculateVariation() {
		if (circularArray.getOccupiedPositions() == 0) {
			return new MercadoBigDecimal(MercadoBigDecimal.NOT_A_NUMBER);
		} else {
			return derivativeLastsSummation
					.divide(new MercadoBigDecimal(circularArray.getOccupiedPositions()));
		}
	}

	private MercadoBigDecimal calculateAverage() {
		if (circularArray.getOccupiedPositions() == 0) {
			return new MercadoBigDecimal(MercadoBigDecimal.NOT_A_NUMBER);
		} else {
			return summation.divide(new MercadoBigDecimal(circularArray.getOccupiedPositions()));
		}
	}

	private MercadoBigDecimal calculateRatio() {
		if (next.equals(MercadoBigDecimal.NOT_A_NUMBER)) {
			return new MercadoBigDecimal(MercadoBigDecimal.NOT_A_NUMBER);
		} else {
			MercadoBigDecimal first;
			if ( base != null) {
				first = base;
			} else {
				first = average;
			}
			return new VariationCalculator().calculate(average, first);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		NonDigitalCurrencyFormatter nonDigitalCurrencyFormatter = NonDigitalCurrencyFormatter.getInstance();
		stringBuilder.append("[ratio: " + new PercentageFormatter().format(ratio));
		stringBuilder.append(", base: " + nonDigitalCurrencyFormatter.format(base));
		stringBuilder.append(", average: " + nonDigitalCurrencyFormatter.format(average));
		stringBuilder.append(", variation: " + nonDigitalCurrencyFormatter.format(variation));
		stringBuilder.append(", next: " + nonDigitalCurrencyFormatter.format(next) + "]");
		return stringBuilder.toString();
	}

	public CircularArray<MercadoBigDecimal> getCircularArray() {
		return circularArray;
	}
}
