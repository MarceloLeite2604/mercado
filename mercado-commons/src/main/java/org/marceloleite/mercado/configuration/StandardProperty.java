package org.marceloleite.mercado.configuration;

public class StandardProperty implements Property {

	private String name;

	private boolean required;

	public StandardProperty(String name, boolean required) {
		super();
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

}
