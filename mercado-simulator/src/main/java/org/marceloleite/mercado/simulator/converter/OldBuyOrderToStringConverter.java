package org.marceloleite.mercado.simulator.converter;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.simulator.CurrencyAmount;
import org.marceloleite.mercado.simulator.order.BuyOrderBuilder.BuyOrder;

public class OldBuyOrderToStringConverter implements Converter<BuyOrder, String> {

	@Override
	public String convertTo(BuyOrder buyOrder) {
		String result = "buy order ";
		CurrencyAmount currencyAmountToBuy = buyOrder.getCurrencyAmountToBuy();
		CurrencyAmount currencyAmountToPay = buyOrder.getCurrencyAmountToPay();
		if ( currencyAmountToBuy.getAmount() != null ) {
			result = "of " + currencyAmountToBuy + " using ";
			if ( currencyAmountToPay.getAmount() != null ) {
				 result += currencyAmountToPay;
			} else {
				result += currencyAmountToPay.getCurrency();
			}
			 
		} else {
			result = "paying " + currencyAmountToPay + " to buy " + currencyAmountToBuy.getCurrency();
		}
		return result;
	}

	@Override
	public BuyOrder convertFrom(String string) {
		throw new UnsupportedOperationException();
	}

}
