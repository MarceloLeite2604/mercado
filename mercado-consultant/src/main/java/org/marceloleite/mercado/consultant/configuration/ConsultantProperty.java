package org.marceloleite.mercado.consultant.configuration;

import org.marceloleite.mercado.properties.Property;
import org.marceloleite.mercado.properties.StandardProperty;

public enum ConsultantProperty implements Property {

	TRADE_RETRIEVE_DURATION("consulting.tradeRetrieveDuration", true),
	TIME_INTERVAL("consulting.timeInterval", true);

	private String name;

	private String value;

	private boolean required;

	private ConsultantProperty(String name, boolean required) {
		this.name = name;
		this.required = required;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		throw new IllegalAccessError();
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public StandardProperty toProperty() {
		return new StandardProperty(name, null, required);
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

}
