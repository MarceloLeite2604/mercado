package org.marceloleite.mercado.configurator.properties;

import org.marceloleite.mercado.commons.properties.Property;

public enum ConfiguratorProperty implements Property {
	
	PERSISTENCE_FILE_NAME("configurator.persistenceFileName", "persistence.properties");
	
	private String name;
	
	private String value;
	
	private String defaultValue;

	private ConfiguratorProperty(String name, String defaultValue) {
		this.name = name;
		this.value = null;
		this.defaultValue = defaultValue;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		throw new UnsupportedOperationException();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isRequired() {
		return (defaultValue == null);
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public boolean isEncrypted() {
		return false;
	}
}
