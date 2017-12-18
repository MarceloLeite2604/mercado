package org.marceloleite.mercado.configuration;

public class StandardConfiguration extends MercadoAbstractConfiguration {

		

	public StandardConfiguration(String configurationFilePath) {
		readConfiguration(configurationFilePath);
	}
	
	public StandardConfiguration() {
		this(DEFAULT_CONFIGURATION_FILE_PATH);
	}

}
