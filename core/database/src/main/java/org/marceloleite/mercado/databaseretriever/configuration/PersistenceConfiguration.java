package org.marceloleite.mercado.databaseretriever.configuration;

import java.util.Properties;

import org.marceloleite.mercado.commons.properties.AbstractPropertiesReader;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.commons.properties.StandardProperty;

public class PersistenceConfiguration extends AbstractPropertiesReader<StandardProperty> {

	private static final String DEFAULT_PERSISTENCE_CONFIGURATION_FILE_NAME = "persistence.properties";

	public PersistenceConfiguration(String configurationFileName) {
		readConfiguration(configurationFileName);
	}

	public PersistenceConfiguration() {
		readConfiguration(DEFAULT_PERSISTENCE_CONFIGURATION_FILE_NAME);
	}

	@Override
	public StandardProperty getTemplateObject() {
		return new StandardProperty();
	}

	public Property getPersistenceProperty(PersistenceProperty persistenceProperty) {
		return getProperty(persistenceProperty);
	}

	public Properties getProperties() {
		Properties properties = new Properties();
		for (PersistenceProperty persistenceProperty : PersistenceProperty.values()) {
			Property property = getPersistenceProperty(persistenceProperty);
			if (null != property) {
				if (property.getValue() == null) {
					if (property.isRequired()) {
						throw new RuntimeException("Property \"" + property.getName() + "\" is not defined.");
					}
				} else {
					properties.setProperty(property.getName(), property.getValue());
				}
			}
		}
		return properties;
	}
}
