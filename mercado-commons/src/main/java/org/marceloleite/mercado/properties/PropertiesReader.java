package org.marceloleite.mercado.properties;

public interface PropertiesReader<E extends Property> {

	void readConfiguration(String configurationFilePath);
	
	E getProperty(E property);
	
	E getTemplateObject();
}
