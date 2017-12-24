package org.marceloleite.mercado.consultant.configuration;

import org.marceloleite.mercado.properties.StandardProperty;

public enum ConsultantProperty {

	CONSULTING_TIME_INTERVAL("consulting.timeInterval", true), 
	CONSULTING_PERIOD("consulting.period", true);

	private String name;

	private boolean required;

	private ConsultantProperty(String name, boolean required) {
		this.name = name;
		this.required = required;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

}
