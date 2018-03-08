package org.marceloleite.mercado.commons.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFileReader {

	private String propertiesFileName;

	public PropertiesFileReader(String propertiesFilePath) {
		super();
		this.propertiesFileName = propertiesFilePath;
	}

	public String getPropertiesFilePath() {
		return propertiesFileName;
	}

	public Properties readPropertiesFile() {
		Properties properties;
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		try (InputStream inputStream = contextClassLoader.getResourceAsStream(propertiesFileName)) {
			properties = new Properties();
			properties.load(inputStream);
		} catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}

		return properties;
	}
}
