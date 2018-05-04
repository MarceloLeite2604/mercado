package org.marceloleite.mercado.commons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

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
}
