package org.marceloleite.mercado.additional;

public class Comparison {

	private double previousValue;

	private double currentValue;

	public Comparison() {
		super();
	}

	public double getPreviousValue() {
		return previousValue;
	}

	public void setPreviousValue(double previousValue) {
		this.previousValue = previousValue;
	}

	public double getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(double currentValue) {
		this.currentValue = currentValue;
	}

	public double calculateDiff() {
		return currentValue - previousValue;
	}

	public double calculatePercentage() {
		return (calculateDiff() / previousValue) * 100.0;
	}

}
