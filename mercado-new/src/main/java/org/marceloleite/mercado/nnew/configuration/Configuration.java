package org.marceloleite.mercado.nnew.configuration;

import java.util.Enumeration;
import java.util.Properties;

public class Configuration {

	public static final String DEFAULT_CONFIGURATION_FILE_PATH = "src/main/resources/application.properties";

	private static Configuration INSTANCE;

	private Properties properties;

	private Configuration(String configurationFilePath) {
		properties = new ConfigurationFileReader(configurationFilePath).readConfigurationFile();
	}

	private Configuration() {
		this(DEFAULT_CONFIGURATION_FILE_PATH);
	}

	public static final Configuration getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Configuration();
		}
		return INSTANCE;
	}

	public void loadConfigurationfile(String configurationFilePath) {
		INSTANCE = new Configuration(configurationFilePath);
	}

	public String getConfiguration(Property property) {

		Enumeration<Object> enumerationKeys = properties.keys();
		while (enumerationKeys.hasMoreElements()) {
			String name = (String) enumerationKeys.nextElement();
			if (property.getName().equals(name)) {
				return properties.getProperty(name);
			}
		}

		throw new RuntimeException("Property \"" + property + "\" not found on configuration file.");
	}

}
