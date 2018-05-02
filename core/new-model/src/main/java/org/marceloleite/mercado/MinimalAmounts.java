package org.marceloleite.mercado;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

import org.marceloleite.mercado.commons.Currency;

public class MinimalAmounts {

	private static MinimalAmounts instance;

	private static final Map<Currency, Double> MINIMAL_AMOUNTS = new EnumMap<>(Currency.class);

	static {
		MINIMAL_AMOUNTS.put(Currency.REAL, 0.01);
		MINIMAL_AMOUNTS.put(Currency.BITCOIN, 0.001);
		MINIMAL_AMOUNTS.put(Currency.LITECOIN, 0.009);
	}

	private MinimalAmounts() {
	}

	public Double retrieveMinimalAmountFor(Currency currency) {
		return MINIMAL_AMOUNTS.getOrDefault(currency, 0.0);
	}

	public boolean isAmountLowerThanMinimal(Currency currency, Double amount) {
		if (currency == null) {
			throw new RuntimeException("Currency to check minimal amount cannot be null.");
		}
		if (amount == null) {
			throw new RuntimeException("Amount of " + currency + " currency to check minimal amount cannot be null.");
		}

		double minimal = retrieveMinimalAmountFor(currency);
		return (amount < minimal);
	}

	public boolean isAmountLowerThanMinimal(Currency currency, BigDecimal amount) {
		return isAmountLowerThanMinimal(currency, amount.doubleValue());
	}

	public boolean isAmountLowerThanMinimal(CurrencyAmount currencyAmount) {
		return isAmountLowerThanMinimal(currencyAmount.getCurrency(), currencyAmount.getAmount());
	}

	public static MinimalAmounts getInstance() {
		if (instance == null) {
			instance = new MinimalAmounts();
		}
		return instance;
	}

}
