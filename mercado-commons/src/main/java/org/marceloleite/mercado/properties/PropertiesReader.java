package org.marceloleite.mercado.properties;

public interface PropertiesReader {

	void readConfiguration(String configurationFilePath);
	
	String getProperty(Property property);
}
