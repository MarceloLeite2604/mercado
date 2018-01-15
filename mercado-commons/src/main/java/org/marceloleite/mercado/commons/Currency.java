package org.marceloleite.mercado.commons;

public enum Currency {

	BITCOIN("BTC", true),
	LITECOIN("LTC", true),
	BCASH("BCH", true),
	REAL("BRL", false);

	private String acronym;

	private boolean digital;

	private Currency(String acronym, boolean digital) {
		this.acronym = acronym;
		this.digital = digital;
	}

	public String getAcronym() {
		return acronym;
	}

	public boolean isDigital() {
		return digital;
	}

	@Override
	public String toString() {
		return getAcronym();
	}

	public static Currency getByAcronym(String acronym) {
		if (acronym == null || acronym.isEmpty()) {
			throw new IllegalArgumentException("Currency acronym cannot be empty.");
		}
		for (Currency currency : values()) {
			if (acronym.toUpperCase().equals(currency.getAcronym().toUpperCase())) {
				return currency;
			}
		}
		throw new IllegalArgumentException("Could not find a currency with acronym \"" + acronym + "\".");
	}

	public static Currency getByName(String currencyName) {
		if (currencyName == null || currencyName.isEmpty()) {
			throw new IllegalArgumentException("Currency name cannot be empty.");
		}
		for (Currency currency : values()) {
			if (currencyName.toUpperCase().equals(currency.name().toUpperCase())) {
				return currency;
			}
		}
		throw new IllegalArgumentException("Could find not a currency with the name \"" + currencyName + "\".");
	}
}
