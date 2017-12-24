package org.marceloleite.mercado.properties;

public interface PropertiesReader<E extends Property> {

	void readConfiguration(String configurationFilePath);
	
	Property getProperty(Property property);
	
	E getTemplateObject();
}
