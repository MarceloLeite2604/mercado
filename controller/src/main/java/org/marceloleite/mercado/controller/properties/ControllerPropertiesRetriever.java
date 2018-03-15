package org.marceloleite.mercado.controller.properties;

import org.marceloleite.mercado.commons.properties.AbstractEnumPropertiesReader;

public class ControllerPropertiesRetriever extends AbstractEnumPropertiesReader<ControllerProperty> {

	private static final String PROPERTIES_FILE_NAME = "controller.properties";

	public ControllerPropertiesRetriever() {
		super(ControllerProperty.class, PROPERTIES_FILE_NAME);
		readConfiguration();
	}

	public String retrievePersistenceFileName() {
		return getProperty(ControllerProperty.PERSISTENCE_FILE).getValue();
	}

	public String retrieveXmlDirectoryPath() {
		return getProperty(ControllerProperty.XML_DIRECTORY_PATH).getValue();
	}

}
