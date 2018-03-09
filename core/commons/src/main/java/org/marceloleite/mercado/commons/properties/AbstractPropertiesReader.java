package org.marceloleite.mercado.commons.properties;

import java.util.Properties;

public abstract class AbstractPropertiesReader<E extends Property> implements PropertiesReader<E> {

	protected static final String DEFAULT_PROPERTIES_FILE_PATH = "application.properties";

	private Properties properties;	

	private PropertiesFileReader propertiesFileReader;

	@Override
	public Property getProperty(Property property) {
		String value = null;
		try {
			value = (String) properties.get(property.getName());
		} catch (NullPointerException nullPointerException) {
			if (property.isRequired()) {
				throw new RuntimeException("Property \"" + property.getName() + "\" not found on configuration file \""
						+ propertiesFileReader.getPropertiesFilePath() + "\".");
			}
		}

		Property retrievedProperty = getTemplateObject();
		retrievedProperty.setName(property.getName());
		retrievedProperty.setValue(value);
		return retrievedProperty;
	}

	@Override
	public void readConfiguration(String configurationFilePath) {
		this.propertiesFileReader = new PropertiesFileReader(configurationFilePath);
		this.properties = propertiesFileReader.readPropertiesFile();
	}

	public void readConfiguration() {
		readConfiguration(DEFAULT_PROPERTIES_FILE_PATH);
	}

}
