package org.marceloleite.mercado.commons.properties;

public class StandardPropertiesReader extends AbstractPropertiesReader<StandardProperty> {

	public StandardPropertiesReader(String propertiesFilePath) {
		readConfiguration(propertiesFilePath);
	}

	public StandardPropertiesReader() {
		this(DEFAULT_PROPERTIES_FILE_PATH);
	}

	@Override
	public StandardProperty getTemplateObject() {
		return new StandardProperty();
	}
}
