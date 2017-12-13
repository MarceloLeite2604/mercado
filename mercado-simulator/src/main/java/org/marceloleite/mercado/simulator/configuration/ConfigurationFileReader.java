package org.marceloleite.mercado.simulator.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationFileReader {

	private String configurationFilePath;

	public ConfigurationFileReader(String configurationFilePath) {
		super();
		this.configurationFilePath = configurationFilePath;
	}

	public Properties readConfigurationFile() {
		Properties properties;
		FileInputStream fileInputStream = null;
		try {
			File file = new File(configurationFilePath);
			fileInputStream = new FileInputStream(file);
			properties = new Properties();
			properties.load(fileInputStream);
		} catch (IOException ioException) {
			throw new RuntimeException(ioException);
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException ioException) {
					throw new RuntimeException(ioException);
				}
			}
		}

		return properties;
	}
	
	public String getConfigurationFilePath() {
		return configurationFilePath;
	}
}
