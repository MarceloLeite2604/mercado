package org.marceloleite.mercado.base.model.order;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.commons.Currency;

public class MinimalAmounts {

	private static final Map<Currency, BigDecimal> MINIMAL_AMOUNTS = new EnumMap<>(Currency.class);

	static {
		MINIMAL_AMOUNTS.put(Currency.REAL, new BigDecimal("0.01"));
		MINIMAL_AMOUNTS.put(Currency.BITCOIN, new BigDecimal("0.001"));
		MINIMAL_AMOUNTS.put(Currency.LITECOIN, new BigDecimal("0.009"));
	}
	
	public static BigDecimal retrieveMinimalAmountFor(Currency currency) {
		return MINIMAL_AMOUNTS.getOrDefault(currency, new BigDecimal("0.0"));
	}
	
	public static boolean isAmountLowerThanMinimal(Currency currency, BigDecimal amount) {
		if (currency == null) {
			throw new RuntimeException("Currency to check minimal amount cannot be null.");
		}
		if (amount == null) {
			throw new RuntimeException("Amount of " + currency + " currency to check minimal amount cannot be null.");
		}

		BigDecimal minimal = retrieveMinimalAmountFor(currency);
		return (amount.compareTo(minimal) < 0);
	}
	
	public static boolean isAmountLowerThanMinimal(CurrencyAmount currencyAmount) {
		return isAmountLowerThanMinimal(currencyAmount.getCurrency(), currencyAmount.getAmount());
	}

	
}
