package org.marceloleite.mercado.data;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;

import org.marceloleite.mercado.commons.Currency;

public class CurrencyAmountData {

	private Currency currency;

	private BigDecimal amount;

	public CurrencyAmountData(Currency currency, BigDecimal amount) {
		super();
		this.currency = currency;
		this.amount = new BigDecimal(amount.toString());
	}

	public Currency getCurrency() {
		return currency;
	}

	@XmlElement
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	@XmlElement
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return currency.getAcronym() + " " + amount;
	}
}
