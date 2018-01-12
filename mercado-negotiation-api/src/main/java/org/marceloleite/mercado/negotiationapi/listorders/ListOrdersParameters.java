package org.marceloleite.mercado.negotiationapi.listorders;

import org.marceloleite.mercado.properties.Property;

public enum ListOrdersParameters implements Property {
	COIN_PAIR("coin_pair", true),
	ORDER_TYPE("order_type", false),
	STATUS_LIST("status_list", false),
	HAS_FILLS("has_fills", false),
	FROM_ID("from_id", false),
	TO_ID("to_id", false),
	FROM_TIMESTAMP("from_timestamp", false),
	TO_TIMESTAMP("to_timestamp", false);

	private String name;

	private String value;

	private boolean required;

	private ListOrdersParameters(String name, boolean required) {
		this.name = name;
		this.required = required;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean isRequired() {
		return this.required;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
