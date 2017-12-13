package org.marceloleite.mercado.simulator;

import org.marceloleite.mercado.consumer.model.Currency;

public class CurrencyAmount {

	private Currency currency;

	private double amount;

	public CurrencyAmount(Currency currency, double amount) {
		super();
		this.currency = currency;
		this.amount = amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return currency.getAcronimo() + " " + amount;
	}
}
