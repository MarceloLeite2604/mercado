package org.marceloleite.mercado.simulator;

import org.marceloleite.mercado.commons.Currency;

public class CurrencyMonitoring {
	
	private Currency currency;

	private double increasePercentage;

	private double decreasePercentage;
	
	private double basePrice;

	public CurrencyMonitoring() {
		super();
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public double getIncreasePercentage() {
		return increasePercentage;
	}

	public void setIncreasePercentage(double increasePercentage) {
		this.increasePercentage = increasePercentage;
	}

	public double getDecreasePercentage() {
		return decreasePercentage;
	}

	public void setDecreasePercentage(double decreasePercentage) {
		this.decreasePercentage = decreasePercentage;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}
}
