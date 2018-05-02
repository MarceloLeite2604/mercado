package org.marceloleite.mercado.databaseretriever.configuration;

import org.marceloleite.mercado.commons.properties.AbstractEnumPropertiesReader;

public class PersistenceConfiguration extends AbstractEnumPropertiesReader<PersistenceProperty> {

	private static final String DEFAULT_PROPERTIES_FILE_NAME = "persistence.properties";
	
	public PersistenceConfiguration(String persistenceConfigurationFileName) {
		super(PersistenceProperty.class, persistenceConfigurationFileName);
	}
	
	public PersistenceConfiguration() {
		super(PersistenceProperty.class, DEFAULT_PROPERTIES_FILE_NAME);
	}
}
