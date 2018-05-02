package org.marceloleite.mercado.commons.properties;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Properties;

import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.commons.encryption.Encrypt;

public abstract class AbstractEnumPropertiesReader<E extends Enum<? extends Property>>
		implements EnumPropertiesReader<E> {

	protected static final String DEFAULT_PROPERTIES_FILE_NAME = "application.properties";

	private Properties properties;

	private PropertiesFileReader propertiesFileReader;

	private String defaultPropertiesFileName;

	private Class<E> propertyEnumClass;

	public AbstractEnumPropertiesReader(Class<E> propertyEnumClass, String defaultPropertiesFileName) {
		this.propertyEnumClass = propertyEnumClass;
		this.defaultPropertiesFileName = defaultPropertiesFileName;
	}

	public AbstractEnumPropertiesReader(Class<E> propertyEnumClass) {
		this(propertyEnumClass, DEFAULT_PROPERTIES_FILE_NAME);
	}

	@Override
	public E getProperty(E property) {
		String value = null;
		Property propertyObject = (Property) property;
		try {
			value = (String) properties.get(propertyObject.getName());

			if (propertyObject.isEncrypted()) {
				String decryptedValue = Encrypt.getInstance().decrypt(value);
				propertyObject.setValue(decryptedValue);
			} else {
				propertyObject.setValue(value);
			}
		} catch (NullPointerException nullPointerException) {
			if (propertyObject.isRequired()) {
				throw new RuntimeException(
						"Property \"" + propertyObject.getName() + "\" not found on configuration file \""
								+ propertiesFileReader.getPropertiesFilePath() + "\".");
			}
		}
		return property;
	}

	@SuppressWarnings("unchecked")
	public Properties getProperties() {
		Properties properties = new Properties();
		Enum<?>[] enumConstants = propertyEnumClass.getEnumConstants();
		for (Enum<?> enumConstant : enumConstants) {
			E propertyToRetrieve = (E) enumConstant;
			E property = getProperty(propertyToRetrieve);
			Property propertyObject = (Property) property;
			if (propertyObject != null) {
				if (propertyObject.getValue() == null) {
					if (propertyObject.isRequired()) {
						throw new RuntimeException("Property \"" + propertyObject.getName() + "\" is not defined.");
					}
				} else {
					properties.setProperty(propertyObject.getName(), propertyObject.getValue());
				}
			}
		}
		return properties;
	}

	@Override
	public void readConfiguration(String configurationFilePath) {
		this.propertiesFileReader = new PropertiesFileReader(configurationFilePath);
		this.properties = propertiesFileReader.readPropertiesFile();
	}

	public void readConfiguration() {
		readConfiguration(defaultPropertiesFileName);
	}

	protected Duration getDurationProperty(E property) {
		Property retrievedProperty = (Property) getProperty(property);
		return Duration.ofSeconds(Long.parseLong(retrievedProperty.getValue()));
	}

	protected ZonedDateTime getZonedDateTimeProperty(E property) {
		Property retrievedProperty = (Property)getProperty(property);
		ZonedDateTimeToStringConverter zonedDateTimeToStringConverter = ZonedDateTimeToStringConverter.getInstance(); 
		return zonedDateTimeToStringConverter.convertFrom(retrievedProperty.getValue());
	}

}
