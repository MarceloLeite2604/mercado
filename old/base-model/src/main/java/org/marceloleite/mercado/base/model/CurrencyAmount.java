package org.marceloleite.mercado.base.model;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.formatter.DigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.formatter.NonDigitalCurrencyFormatter;
import org.marceloleite.mercado.data.CurrencyAmountData;

public class CurrencyAmount {

	private Currency currency;

	private MercadoBigDecimal amount;

	public CurrencyAmount(Currency currency, MercadoBigDecimal amount) {
		super();
		this.currency = currency;
		setAmount(new MercadoBigDecimal(amount.toString()));
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

	public MercadoBigDecimal getAmount() {
		return amount;
	}

	public void setAmount(MercadoBigDecimal amount) {
		int scale = 0;
		if ( currency == null ) {
			scale = MercadoBigDecimal.DEFAULT_SCALE;
		} else {
			scale = currency.getScale();
		}
		amount.setScale(scale);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurrencyAmount other = (CurrencyAmount) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (currency != other.currency)
			return false;
		return true;
	}
}
