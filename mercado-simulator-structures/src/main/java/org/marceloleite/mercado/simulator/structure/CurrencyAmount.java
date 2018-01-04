package org.marceloleite.mercado.simulator.structure;

import javax.xml.bind.annotation.XmlElement;

import org.marceloleite.mercado.commons.Currency;

public class CurrencyAmount {

	private Currency currency;

	private Double amount;

	public CurrencyAmount(Currency currency, Double amount) {
		super();
		this.currency = currency;
		this.amount = amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	@XmlElement
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Double getAmount() {
		return amount;
	}

	@XmlElement
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return currency.getAcronym() + " " + amount;
	}
}
