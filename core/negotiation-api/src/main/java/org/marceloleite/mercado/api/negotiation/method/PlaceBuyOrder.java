package org.marceloleite.mercado.api.negotiation.method;

import org.marceloleite.mercado.CurrencyPair;
import org.marceloleite.mercado.MinimalAmounts;
import org.marceloleite.mercado.commons.utils.formatter.DigitalCurrencyFormatter;
import org.marceloleite.mercado.commons.utils.formatter.NonDigitalCurrencyFormatter;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.TapiInformation;

public class PlaceBuyOrder extends TapiMethodTemplate<TapiResponse<Order>> {

	private static final String[] PARAMETER_NAMES = { "coin_pair", "quantity", "limit_price" };

	public PlaceBuyOrder(TapiInformation tapiInformation) {
		super(tapiInformation, TapiMethod.PLACE_BUY_ORDER, PARAMETER_NAMES);
	}

	public TapiResponse<Order> execute(CurrencyPair currencyPair, Double quantity, Double limitPrice) {
		checkLimits(currencyPair, quantity, limitPrice);
		return executeMethod(currencyPair, quantity, limitPrice);
	}

	private void checkLimits(CurrencyPair currencyPair, Double quantity, Double limitPrice) {

		if (MinimalAmounts.getInstance()
				.isAmountLowerThanMinimal(currencyPair.getFirstCurrency(), limitPrice)) {
			throw new RuntimeException("LimitPrice " + NonDigitalCurrencyFormatter.format(limitPrice)
					+ " is below minimal accepted value for \"" + currencyPair.getFirstCurrency() + "\" currency.");
		}

		if (MinimalAmounts.getInstance()
				.isAmountLowerThanMinimal(currencyPair.getSecondCurrency(), quantity)) {
			throw new RuntimeException("Quantity " + DigitalCurrencyFormatter.format(quantity)
					+ " is below minimal accepted value for \"" + currencyPair.getSecondCurrency() + "\" currency.");
		}
	}
}
