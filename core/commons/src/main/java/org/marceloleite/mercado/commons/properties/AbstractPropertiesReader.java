package org.marceloleite.mercado.commons.properties;

import java.util.Properties;

import org.marceloleite.mercado.commons.encryption.Encrypt;

public abstract class AbstractPropertiesReader<E extends Property> implements PropertiesReader<E> {

	protected static final String DEFAULT_PROPERTIES_FILE_NAME = "application.properties";

	private Properties properties;

	private PropertiesFileReader propertiesFileReader;
	
	private String defaultPropertiesFileName;
	
	public AbstractPropertiesReader(String defaultPropertiesFileName) {
		this.defaultPropertiesFileName = defaultPropertiesFileName;
		readConfiguration();
	}
	
	public AbstractPropertiesReader() {
		this(DEFAULT_PROPERTIES_FILE_NAME);
	}

	@Override
	public E getProperty(E property) {
		String value = null;
		Property propertyObject = (Property)property;
		try {
			value = (String) properties.get(propertyObject.getName());
			
			if ( propertyObject.isEncrypted()) {
				String decryptedValue = new Encrypt().decrypt(value);
				propertyObject.setValue(decryptedValue);
			} else {
				propertyObject.setValue(value);
			}
		} catch (NullPointerException nullPointerException) {
			if (propertyObject.isRequired()) {
				throw new RuntimeException("Property \"" + propertyObject.getName() + "\" not found on configuration file \""
						+ propertiesFileReader.getPropertiesFilePath() + "\".");
			}
		}
		return property;
	}
	
	@Override
	public void readConfiguration(String configurationFilePath) {
		this.propertiesFileReader = new PropertiesFileReader(configurationFilePath);
		this.properties = propertiesFileReader.readPropertiesFile();
	}

	public void readConfiguration() {
		readConfiguration(defaultPropertiesFileName);
	}

}
