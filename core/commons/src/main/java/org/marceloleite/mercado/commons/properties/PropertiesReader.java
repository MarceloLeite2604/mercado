package org.marceloleite.mercado.commons.properties;

public interface PropertiesReader<E extends Property> {

	void readConfiguration(String configurationFilePath);
	
	Property getProperty(Property property);
	
	E getTemplateObject();
}
