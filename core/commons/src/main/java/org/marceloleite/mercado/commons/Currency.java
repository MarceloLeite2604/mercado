package org.marceloleite.mercado.commons;

public enum Currency {

	BITCOIN("BTC", true, 8),
	LITECOIN("LTC", true, 8),
	BCASH("BCH", true, 8),
	BGOLD("BTG", true, 8),
	REAL("BRL", false, 5);

	private String acronym;

	private boolean digital;
	
	private int scale;

	private Currency(String acronym, boolean digital, int scale) {
		this.acronym = acronym;
		this.digital = digital;
		this.scale = scale;
	}

	public String getAcronym() {
		return acronym;
	}

	public boolean isDigital() {
		return digital;
	}
	
	public int getScale() {
		return scale;
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
