package org.marceloleite.mercado.simulator;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.simulator.structure.CurrencyAmountData;

public class CurrencyAmount {

	private Currency currency;

	private Double amount;

	public CurrencyAmount(Currency currency, Double amount) {
		super();
		this.currency = currency;
		this.amount = amount;
	}

	public CurrencyAmount(CurrencyAmountData currencyAmountData) {
		this(currencyAmountData.getCurrency(), currencyAmountData.getAmount());
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String toString() {
		return currency.getAcronym() + " " + amount;
	}
}
