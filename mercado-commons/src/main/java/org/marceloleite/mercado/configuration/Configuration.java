package org.marceloleite.mercado.configuration;

public interface Configuration {

	void readConfiguration(String configurationFilePath);
	
	String getProperty(Property property);
}
