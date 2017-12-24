package org.marceloleite.mercado.databaseretriever.configuration;

import java.util.Properties;

import org.marceloleite.mercado.properties.AbstractPropertiesReader;
import org.marceloleite.mercado.properties.StandardProperty;

public class PersistenceConfiguration extends AbstractPropertiesReader<StandardProperty> {

	private static final String PERSISTENCE_CONFIGURATION_FILE_PATH = "src/main/resources/persistence.properties";

	public PersistenceConfiguration(String configurationFilePath) {
		readConfiguration(configurationFilePath);
	}

	public PersistenceConfiguration() {
		readConfiguration(PERSISTENCE_CONFIGURATION_FILE_PATH);
	}

	@Override
	public StandardProperty getTemplateObject() {
		return new StandardProperty();
	}

	public StandardProperty getPersistenceProperty(PersistenceProperty persistenceProperty) {
		return getProperty(persistenceProperty.toProperty());
	}

	public Properties getProperties() {
		Properties properties = new Properties();
		for (PersistenceProperty persistenceProperty : PersistenceProperty.values()) {
			StandardProperty standardProperty = getPersistenceProperty(persistenceProperty);
			if (null != standardProperty) {
				properties.setProperty(standardProperty.getName(), standardProperty.getValue());
			}
		}
		return properties;
	}

	/*
	 * @Override public StandardProperty getProperty(StandardProperty property) {
	 * return getProperty(property); }
	 * 
	 * @Override public StandardProperty getTemplateObject() { // TODO
	 * Auto-generated method stub return null; }
	 */
}
