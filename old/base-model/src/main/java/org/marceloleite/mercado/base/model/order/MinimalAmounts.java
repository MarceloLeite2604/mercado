package org.marceloleite.mercado.base.model.order;

import java.util.EnumMap;
import java.util.Map;

import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;

public class MinimalAmounts {

	private static final Map<Currency, MercadoBigDecimal> MINIMAL_AMOUNTS = new EnumMap<>(Currency.class);

	static {
		MINIMAL_AMOUNTS.put(Currency.REAL, new MercadoBigDecimal("0.01"));
		MINIMAL_AMOUNTS.put(Currency.BITCOIN, new MercadoBigDecimal("0.001"));
		MINIMAL_AMOUNTS.put(Currency.LITECOIN, new MercadoBigDecimal("0.009"));
	}
	
	public static MercadoBigDecimal retrieveMinimalAmountFor(Currency currency) {
		return MINIMAL_AMOUNTS.getOrDefault(currency, new MercadoBigDecimal("0.0"));
	}
	
	public static boolean isAmountLowerThanMinimal(Currency currency, MercadoBigDecimal amount) {
		if (currency == null) {
			throw new RuntimeException("Currency to check minimal amount cannot be null.");
		}
		if (amount == null) {
			throw new RuntimeException("Amount of " + currency + " currency to check minimal amount cannot be null.");
		}

		MercadoBigDecimal minimal = retrieveMinimalAmountFor(currency);
		return (amount.compareTo(minimal) < 0);
	}
	
	public static boolean isAmountLowerThanMinimal(CurrencyAmount currencyAmount) {
		return isAmountLowerThanMinimal(currencyAmount.getCurrency(), currencyAmount.getAmount());
	}

	
}
