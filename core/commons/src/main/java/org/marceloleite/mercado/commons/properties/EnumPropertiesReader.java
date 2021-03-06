package org.marceloleite.mercado.commons.properties;

import java.util.Properties;

public interface EnumPropertiesReader<E extends Enum<? extends Property>> {
	
	void readConfiguration(String configurationFilePath);
	
	E getProperty(E property);

	Properties getProperties();
}
