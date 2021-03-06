package org.marceloleite.mercado.base.model;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.formatter.DigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.formatter.NonDigitalCurrencyFormatter;
import org.marceloleite.mercado.data.CurrencyAmountData;

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

	public CurrencyAmount(CurrencyAmount currencyAmount) {
		this(currencyAmount.getCurrency(), currencyAmount.getAmount());
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
		String stringAmount;
		if (currency.isDigital()) {
			stringAmount = new DigitalCurrencyFormatter().format(amount);
		} else {
			stringAmount = new NonDigitalCurrencyFormatter().format(amount);
		}
		return currency.getAcronym() + " " + stringAmount;
	}
}
