package org.marceloleite.mercado.commons.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesFileReader {

	private String propertiesFilePath;

	public PropertiesFileReader(String propertiesFilePath) {
		super();
		this.propertiesFilePath = propertiesFilePath;
	}

	public String getPropertiesFilePath() {
		return propertiesFilePath;
	}

	public Properties readPropertiesFile() {
		Properties properties;
		FileInputStream fileInputStream = null;
		try {
			File file = new File(propertiesFilePath);
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
}
