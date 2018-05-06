package org.marceloleite.mercado.commons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.marceloleite.mercado.commons.properties.PropertyDefinition;
import org.marceloleite.mercado.commons.utils.creator.ObjectMapperCreator;

public class PropertiesUtils {

	private PropertiesUtils() {
	}

	public static Properties retrieveProperties(String persistenceFileName) {

		Properties properties = new Properties();
		try (InputStream inputStream = PropertiesUtils.class.getClassLoader()
				.getResourceAsStream(persistenceFileName)) {

			properties.load(inputStream);
			inputStream.close();
		} catch (IOException exception) {
			throw new RuntimeException("Error while reading \"" + persistenceFileName + "\" properties file.",
					exception);
		}

		return properties;
	}

	public static List<String> getNames(Properties properties) {
		return properties.keySet()
				.stream()
				.map(object -> (String) object)
				.collect(Collectors.toList());
	}

	public static Properties getPropertiesStartingWith(Properties properties, String prefix) {
		Properties matchingProperties = new Properties();
		properties.entrySet()
				.stream()
				.filter(entry -> ((String) entry.getKey()).startsWith("hibernate"))
				.forEach(entry -> matchingProperties.setProperty((String) entry.getKey(), (String) entry.getValue()));
		return matchingProperties;
	}

	public static String retrieveProperty(Properties properties, PropertyDefinition propertyDefinition) {
		String value = properties.getProperty(propertyDefinition.getName(), propertyDefinition.getDefaultValue());
		if (StringUtils.isEmpty(value) && propertyDefinition.isRequired()) {
			throw new RuntimeException("Could not find persistence property \"" + propertyDefinition.getName() + "\".");
		}
		if (propertyDefinition.isEncrypted()) {
			value = EncryptUtils.decrypt(value);
		}
		return value;
	}

	public static <T> T retrievePropertyAs(Properties properties, PropertyDefinition propertyDefinition,
			Class<T> propertyClass) {
		String value = retrieveProperty(properties, propertyDefinition);
		T result = null;
		try {
			result = ObjectMapperCreator.create()
					.readValue(value, propertyClass);
		} catch (IOException exception) {
			throw new RuntimeException("Error while convereting \"" + propertyDefinition.getName() + "\" property to \""
					+ propertyClass.getName() + "\" class.", exception);
		}
		return result;
	}
}
