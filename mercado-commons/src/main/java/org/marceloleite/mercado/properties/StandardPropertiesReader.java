package org.marceloleite.mercado.properties;

public class StandardPropertiesReader extends AbstractPropertiesReader {

	public StandardPropertiesReader(String propertiesFilePath) {
		readConfiguration(propertiesFilePath);
	}

	public StandardPropertiesReader() {
		this(DEFAULT_PROPERTIES_FILE_PATH);
	}

}
