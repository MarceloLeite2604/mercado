package org.marceloleite.mercado.negotiationapi.model;

import org.marceloleite.mercado.commons.Currency;

public enum CurrencyPair {

	BRLBTC(Currency.REAL, Currency.BITCOIN),
	BRLLTC(Currency.REAL, Currency.LITECOIN),
	BRLBCH(Currency.REAL, Currency.BCASH);

	private Currency firstCurrency;

	private Currency secondCurrency;

	private CurrencyPair(Currency firstCurrency, Currency secondCurrency) {
		this.firstCurrency = firstCurrency;
		this.secondCurrency = secondCurrency;
	}

	public Currency getFirstCurrency() {
		return firstCurrency;
	}

	public Currency getSecondCurrency() {
		return secondCurrency;
	}

	@Override
	public String toString() {
		return firstCurrency.toString() + secondCurrency.toString();
	}

	public static CurrencyPair retrieveByPairAcronym(String currencyPairAcronyms) {
		if (currencyPairAcronyms == null) {
			throw new IllegalArgumentException("Currency pair acronyms cannot be null.");
		}
		if (currencyPairAcronyms.length() != 6) {
			throw new IllegalArgumentException("Invalid currency pair acronyms value: " + currencyPairAcronyms);
		}
		String firstCoinPairAcronym = currencyPairAcronyms.substring(0, 3);
		Currency firstCurrency = Currency.getByAcronym(firstCoinPairAcronym);
		String secondCoinPairAcronym = currencyPairAcronyms.substring(3, 6);
		Currency secondCurrency = Currency.getByAcronym(secondCoinPairAcronym);
		for (CurrencyPair currencyPair : values()) {
			if (currencyPair.getFirstCurrency().equals(firstCurrency)
					&& currencyPair.getSecondCurrency().equals(secondCurrency)) {
				return currencyPair;
			}
		}
		
		throw new IllegalArgumentException("Could not find currency pair fro acronym: " + currencyPairAcronyms);
	}
}
