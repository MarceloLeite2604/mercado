package org.marceloleite.mercado.controller.properties;

import org.marceloleite.mercado.commons.properties.Property;

public enum ControllerProperty implements Property {

	PERSISTENCE_FILE("controller.persistenceFile", "persistence.properties"),
	XML_DIRECTORY_PATH("controller.xmlDirectoryPath", null),
	TRADES_SITE_RETRIEVER_THREAD_POOL_SIZE("controller.tradesSiteRetriever.threadPoolSize", "8"),
	TRADES_SITE_RETRIEVER_DURATION_STEP("controller.tradesSiteRetriever.durationStep", "30");
	
	private ControllerProperty(String name, String defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

	private String name;

	private String value;
	
	private String defaultValue;

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
		return (defaultValue != null);
	}

	public boolean isEncrypted() {
		return false;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
}
