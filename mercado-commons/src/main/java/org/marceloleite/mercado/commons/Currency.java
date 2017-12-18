package org.marceloleite.mercado.commons;

public enum Currency {

	BITCOIN("BTC", true), LITECOIN("LTC", true), BCASH("BCH", true), REAL("BRL", false);

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
		for (Currency currency : values()) {
			if (acronym.equals(currency.getAcronym())) {
				return currency;
			}
		}
		return null;
	}
}
