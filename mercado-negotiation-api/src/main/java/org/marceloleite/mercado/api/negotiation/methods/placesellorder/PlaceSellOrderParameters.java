package org.marceloleite.mercado.api.negotiation.methods.placesellorder;

public enum PlaceSellOrderParameters {
	COIN_PAIR("coin_pair"),
	QUANTITY("quantity"),
	LIMIT_PRICE("limit_price");
	
	private String name;

	private PlaceSellOrderParameters(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
