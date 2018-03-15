package org.marceloleite.mercado.commons.properties;

public class StandardProperty implements Property {

	private String name;

	private String value;

	private String defaultValue;

	private boolean encrypted;

	public StandardProperty(String name, String value, Boolean encrypted, String defaultValue) {
		super();
		this.name = name;
		this.value = value;
		this.defaultValue = defaultValue;
		this.encrypted = encrypted;
	}

	public StandardProperty(String name, String value, boolean encrypted) {
		this(name, value, encrypted, null);
	}

	public StandardProperty(Property property) {
		super();

		if (property == null) {
			throw new IllegalArgumentException("Property cannot be null.");
		}

		this.name = new String(property.getName());
		if (property.getValue() != null) {
			this.value = new String(property.getValue());
		}

		if (property.getDefaultValue() != null) {
			this.defaultValue = new String(property.getDefaultValue());
		}
		
		this.encrypted = new Boolean(property.isEncrypted());
	}

	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean isRequired() {
		return (defaultValue == null);
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public boolean isEncrypted() {
		return encrypted;
	}
}
