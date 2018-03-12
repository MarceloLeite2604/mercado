package org.marceloleite.mercado.controller.properties;

import org.marceloleite.mercado.commons.properties.Property;

public enum ControllerProperty implements Property {

	PERSISTENCE_FILE("controller.persistenceFile", false, false, "persistence.properties"),
	XML_DIRECTORY_PATH("controller.xmlDirectoryPath", true, false);
	
	private ControllerProperty(String name, boolean required, boolean encrypted, String defaultValue) {
		this.name = name;
		this.required = required;
		this.encrypted = encrypted;
		this.defaultValue = defaultValue;
	}

	private ControllerProperty(String name, boolean required, boolean encrypted) {
		this(name, required, encrypted, null);
	}

	private String name;

	private String value;
	
	private String defaultValue;

	private boolean required;

	private boolean encrypted;

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
		return required;
	}

	public boolean isEncrypted() {
		return encrypted;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
}
