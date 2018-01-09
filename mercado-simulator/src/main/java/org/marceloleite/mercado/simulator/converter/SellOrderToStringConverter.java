package org.marceloleite.mercado.simulator.converter;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.simulator.CurrencyAmount;
import org.marceloleite.mercado.simulator.order.SellOrder;

public class SellOrderToStringConverter implements Converter<SellOrder, String> {

	@Override
	public String convertTo(SellOrder sellOrder) {
		String result;
		CurrencyAmountToStringConverter currencyAmountToStringConverter = new CurrencyAmountToStringConverter();
		CurrencyAmount currencyAmountToSell = sellOrder.getCurrencyAmountToSell();
		CurrencyAmount currencyAmountToReceive = sellOrder.getCurrencyAmountToReceive();
		if (currencyAmountToSell.getAmount() != null) {
			result = "sell order of " + currencyAmountToStringConverter.convertTo(currencyAmountToSell) + " receiving ";
			if (currencyAmountToReceive.getAmount() != null) {
				result += currencyAmountToStringConverter.convertTo(currencyAmountToReceive);
			} else {
				result += currencyAmountToReceive.getCurrency().getAcronym();
			}

		} else {
			result = "sell order receiving " + currencyAmountToStringConverter.convertTo(currencyAmountToReceive)
					+ " to sell " + currencyAmountToSell.getCurrency().getAcronym();
		}
		return result;
	}

	@Override
	public SellOrder convertFrom(String string) {
		throw new UnsupportedOperationException();
	}

}
