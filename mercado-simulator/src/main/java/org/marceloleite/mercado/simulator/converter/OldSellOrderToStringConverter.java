package org.marceloleite.mercado.simulator.converter;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.simulator.CurrencyAmount;
import org.marceloleite.mercado.simulator.order.SellOrderBuilder.SellOrder;

public class OldSellOrderToStringConverter implements Converter<SellOrder, String> {

	@Override
	public String convertTo(SellOrder sellOrder) {
		String result = "sell order ";
		CurrencyAmount currencyAmountToSell = sellOrder.getCurrencyAmountToSell();
		CurrencyAmount currencyAmountToReceive = sellOrder.getCurrencyAmountToReceive();
		if (currencyAmountToSell.getAmount() != null) {
			result = "of " + currencyAmountToSell + " receiving ";
			if (currencyAmountToReceive.getAmount() != null) {
				result += currencyAmountToReceive;
			} else {
				result += currencyAmountToReceive.getCurrency() + " currency";
			}

		} else {
			result = "receiving " + currencyAmountToReceive + " by selling "
					+ currencyAmountToSell.getCurrency() + " currency";
		}
		return result;
	}

	@Override
	public SellOrder convertFrom(String string) {
		throw new UnsupportedOperationException();
	}

}
