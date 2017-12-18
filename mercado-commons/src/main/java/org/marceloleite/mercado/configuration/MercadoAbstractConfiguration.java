package org.marceloleite.mercado.configuration;

import java.util.Properties;

public class MercadoAbstractConfiguration implements Configuration {
	
	protected static final String DEFAULT_CONFIGURATION_FILE_PATH = "application.properties";

	private Properties properties;
	
	private ConfigurationFileReader configurationFileReader;

	@Override
	public String getProperty(Property property) {
		String value = null;
		try {
			value = (String) properties.get(property.getName());
		} catch (NullPointerException nullPointerException) {
			if (!property.isRequired()) {
				throw new RuntimeException("Property \"" + property.getName() + "\" not found on configuration file \""
						+ configurationFileReader.getConfigurationFilePath() + "\".");
			}
		}
		return value;
	}

	@Override
	public void readConfiguration(String configurationFilePath) {
		this.configurationFileReader = new ConfigurationFileReader(configurationFilePath);
		this.properties = configurationFileReader.readConfigurationFile();
	}

}
