package org.marceloleite.mercado.consumer.model;

public enum Currency {

	BITCOIN("BTC", true), LITECOIN("LTC", true), BCASH("BCH", true), REAL("BRL", false);

	private String acronimo;

	private boolean digital;

	private Currency(String acronimo, boolean digital) {
		this.acronimo = acronimo;
		this.digital = digital;
	}

	public String getAcronimo() {
		return acronimo;
	}

	public boolean isDigital() {
		return digital;
	}

	@Override
	public String toString() {
		return getAcronimo();
	}

	public static Currency getByAcronimo(String acronimo) {
		for (Currency currency : values()) {
			if (acronimo.equals(currency.getAcronimo())) {
				return currency;
			}
		}
		return null;
	}
}
