package org.marceloleite.mercado.simulator.converter;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.simulator.BuyOrder;
import org.marceloleite.mercado.simulator.CurrencyAmount;

public class BuyOrderToStringConverter implements Converter<BuyOrder, String> {

	@Override
	public String convertTo(BuyOrder buyOrder) {
		String result;
		CurrencyAmountToStringConverter currencyAmountToStringConverter = new CurrencyAmountToStringConverter();
		CurrencyAmount currencyAmountToBuy = buyOrder.getCurrencyAmountToBuy();
		CurrencyAmount currencyAmountToPay = buyOrder.getCurrencyAmountToPay();
		if ( currencyAmountToBuy.getAmount() != null ) {
			result = "Buy order of " + currencyAmountToStringConverter.convertTo(currencyAmountToBuy) + " using ";
			if ( currencyAmountToPay.getAmount() != null ) {
				 result += currencyAmountToStringConverter.convertTo(currencyAmountToPay);
			} else {
				result += currencyAmountToPay.getCurrency().getAcronym();
			}
			 
		} else {
			result = "Buy order paying " + currencyAmountToStringConverter.convertTo(currencyAmountToPay) + " to buy " + currencyAmountToBuy.getCurrency().getAcronym();
		}
		return result;
	}

	@Override
	public BuyOrder convertFrom(String string) {
		throw new UnsupportedOperationException();
	}

}
