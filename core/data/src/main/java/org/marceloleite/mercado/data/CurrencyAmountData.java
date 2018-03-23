package org.marceloleite.mercado.data;

import javax.xml.bind.annotation.XmlElement;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;

public class CurrencyAmountData {

	private Currency currency;

	private MercadoBigDecimal amount;

	public CurrencyAmountData(Currency currency, MercadoBigDecimal amount) {
		super();
		this.currency = currency;
		this.amount = new MercadoBigDecimal(amount.toString());
	}

	public Currency getCurrency() {
		return currency;
	}

	@XmlElement
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public MercadoBigDecimal getAmount() {
		return amount;
	}

	@XmlElement
	public void setAmount(MercadoBigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return currency.getAcronym() + " " + amount;
	}
}
