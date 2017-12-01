package org.marceloleite.mercado.model;

public enum Cryptocoin {

	BITCOIN("BTC"), LITECOIN("LTC"), BCASH("BCH");

	private String acronimo;

	private Cryptocoin(String acronimo) {
		this.acronimo = acronimo;
	}

	public String getAcronimo() {
		return acronimo;
	}

	@Override
	public String toString() {
		return getAcronimo();
	}
}
