package org.marceloleite.mercado.api.negotiation.methods.listorders;

public enum ListOrdersParameters {
	COIN_PAIR("coin_pair"),
	ORDER_TYPE("order_type"),
	STATUS_LIST("status_list"),
	HAS_FILLS("has_fills"),
	FROM_ID("from_id"),
	TO_ID("to_id"),
	FROM_TIMESTAMP("from_timestamp"),
	TO_TIMESTAMP("to_timestamp");

	private String name;

	private ListOrdersParameters(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
