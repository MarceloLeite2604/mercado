package org.marceloleite.mercado.api.negotiation.methods.placebuyorder;

public enum PlaceBuyOrderParameters {

	COIN_PAIR("coin_pair"),
	QUANTITY("quantity"),
	LIMIT_PRICE("limit_price");

	private PlaceBuyOrderParameters(String name) {
		this.name = name;
	}

	private String name;

	@Override
	public String toString() {
		return name;
	}
}
