package org.marceloleite.mercado.api.negotiation.methods.getorder;

public enum GetOrderParameters {

	COIN_PAIR("coin_pair"),
	ORDER_ID("order_id");

	private String name;

	private GetOrderParameters(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
