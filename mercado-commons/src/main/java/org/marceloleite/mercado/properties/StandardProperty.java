package org.marceloleite.mercado.properties;

public class StandardProperty implements Property {

	private String name;

	private String value;

	private boolean required;
	
	public StandardProperty() {
		super();
	}
	
	public StandardProperty(String name, String value, boolean required) {
		super();
		this.name = name;
		this.required = required;
	}

	public StandardProperty(String name, boolean required) {
		this(name, null, required);
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
