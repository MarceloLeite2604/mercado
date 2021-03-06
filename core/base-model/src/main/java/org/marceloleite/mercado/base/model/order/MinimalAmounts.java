package org.marceloleite.mercado.base.model.order;

import java.util.EnumMap;
import java.util.Map;

import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.commons.Currency;

public class MinimalAmounts {

	private static final Map<Currency, Double> MINIMAL_AMOUNTS = new EnumMap<>(Currency.class);

	static {
		MINIMAL_AMOUNTS.put(Currency.REAL, 0.01);
		MINIMAL_AMOUNTS.put(Currency.BITCOIN, 0.001);
		MINIMAL_AMOUNTS.put(Currency.LITECOIN, 0.009);
	}
	
	public static Double retrieveMinimalAmountFor(Currency currency) {
		return MINIMAL_AMOUNTS.getOrDefault(currency, 0.0);
	}
	
	public static boolean isAmountLowerThanMinimal(Currency currency, Double amount) {
		if (currency == null) {
			throw new RuntimeException("Currency to check minimal amount cannot be null.");
		}
		if (amount == null) {
			throw new RuntimeException("Amount of " + currency + " currency to check minimal amount cannot be null.");
		}

		Double minimal = retrieveMinimalAmountFor(currency);
		return (amount < minimal);
	}
	
	public static boolean isAmountLowerThanMinimal(CurrencyAmount currencyAmount) {
		return isAmountLowerThanMinimal(currencyAmount.getCurrency(), currencyAmount.getAmount());
	}

	
}
