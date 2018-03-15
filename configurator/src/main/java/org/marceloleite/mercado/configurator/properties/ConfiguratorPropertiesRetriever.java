package org.marceloleite.mercado.configurator.properties;

import org.marceloleite.mercado.commons.properties.AbstractEnumPropertiesReader;

public class ConfiguratorPropertiesRetriever extends AbstractEnumPropertiesReader<ConfiguratorProperty>{
	
	private static final String PROPERTIES_FILE_NAME = "configurator.properties";
	
	public ConfiguratorPropertiesRetriever() {
		super(ConfiguratorProperty.class, PROPERTIES_FILE_NAME);
	}
}
