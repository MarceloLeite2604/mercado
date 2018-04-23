package org.marceloleite.mercado;

import java.math.BigDecimal;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
import org.marceloleite.mercado.commons.formatter.DigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.formatter.NonDigitalCurrencyFormatter;

public class CurrencyAmount {

	private Currency currency;

	private BigDecimal amount;

	public CurrencyAmount(Currency currency, BigDecimal amount) {
		super();
		this.currency = currency;
		setAmount(new MercadoBigDecimal(amount.toString()));
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
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
			stringAmount = DigitalCurrencyFormatter.getInstance().format(amount);
		} else {
			stringAmount = NonDigitalCurrencyFormatter.getInstance().format(amount);
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
