package org.marceloleite.mercado.simulator.converter;

import java.text.DecimalFormat;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.simulator.CurrencyAmount;

public class CurrencyAmountToStringConverter implements Converter<CurrencyAmount, String> {
	
	private static final String DIGITAL_CURRENCY_FORMAT = "#.0000";
	
	private static final String NON_DIGITAL_CURRENCY_FORMAT = "#.00";

	@Override
	public String convertTo(CurrencyAmount currencyAmount) {
		DecimalFormat decimalFormat;
		
		Currency currency = currencyAmount.getCurrency();
		if ( currency.isDigital() ) {
			decimalFormat = new DecimalFormat(DIGITAL_CURRENCY_FORMAT);
		} else {
			decimalFormat = new DecimalFormat(NON_DIGITAL_CURRENCY_FORMAT);
		}
		
		double amount = currencyAmount.getAmount();
		return decimalFormat.format(amount) + " " + currency.getAcronym();
	}

	@Override
	public CurrencyAmount convertFrom(String object) {
		// TODO Auto-generated method stub
		return null;
	}

}
