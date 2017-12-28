package org.marceloleite.mercado.consultant.property;

import org.marceloleite.mercado.properties.Property;
import org.marceloleite.mercado.properties.StandardProperty;

public enum ConsultantProperty implements Property {

	BACKWARD_EXECUTE("consulting.backward.execute", true),
	BACKWARD_START_TIME("consulting.backward.startTime", false),
	BACKWARD_END_TIME("consulting.backward.endTime", false),
	BACKWARD_TRADE_RETRIEVE_DURATION("consulting.backward.tradeRetrieveDuration", true),
	BACKWARD_TIME_INTERVAL("consulting.backward.timeInterval", true),
	FORWARD_EXECUTE("consulting.forward.execute", true),
	FORWARD_START_TIME("consulting.forward.startTime", false),
	FORWARD_END_TIME("consulting.forward.endTime", false),
	FORWARD_TRADE_RETRIEVE_DURATION("consulting.forward.tradeRetrieveDuration", true),
	FORWARD_TIME_INTERVAL("consulting.forward.timeInterval", true);

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
