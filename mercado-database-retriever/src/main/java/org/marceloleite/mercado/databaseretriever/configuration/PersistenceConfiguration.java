package org.marceloleite.mercado.databaseretriever.configuration;

import java.util.Properties;

import org.marceloleite.mercado.properties.AbstractPropertiesReader;

public class PersistenceConfiguration extends AbstractPropertiesReader {

	private static final String PERSISTENCE_CONFIGURATION_FILE_PATH = "src/main/resources/persistence.properties";

	public PersistenceConfiguration() {
		readConfiguration(PERSISTENCE_CONFIGURATION_FILE_PATH);
	}

	public String getPersistenceProperty(PersistenceProperty persistenceProperty) {
		return getProperty(persistenceProperty.toProperty());
	}

	public Properties getProperties() {
		Properties properties = new Properties();
		for (PersistenceProperty persistenceProperty : PersistenceProperty.values()) {
			String propertyValue = getPersistenceProperty(persistenceProperty);
			if (null != propertyValue && !propertyValue.isEmpty()) {
				properties.setProperty(persistenceProperty.getName(), propertyValue);
			}
		}
		return properties;
	}
}
