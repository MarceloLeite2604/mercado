package org.marceloleite.mercado.base.model.converter;

import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.commons.formatter.DigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.formatter.NonDigitalCurrencyFormatter;

public class CurrencyAmountToStringConverter implements Converter<CurrencyAmount, String> {

	@Override
	public String convertTo(CurrencyAmount currencyAmount) {

		Currency currency = currencyAmount.getCurrency();
		double amount = currencyAmount.getAmount();
		String stringAmount;
		if (currency.isDigital()) {
			stringAmount = new DigitalCurrencyFormatter().format(amount);
		} else {
			stringAmount = new NonDigitalCurrencyFormatter().format(amount);
		}

		return stringAmount + " " + currency;
	}

	@Override
	public CurrencyAmount convertFrom(String object) {
		throw new UnsupportedOperationException();
	}

}
