package org.marceloleite.mercado.controller.properties;

<<<<<<< HEAD
import java.time.Duration;

import org.marceloleite.mercado.commons.properties.AbstractEnumPropertiesReader;

public class ControllerPropertiesRetriever extends AbstractEnumPropertiesReader<ControllerProperty> {

	private static final String PROPERTIES_FILE_NAME = "controller.properties";

	public ControllerPropertiesRetriever() {
		super(ControllerProperty.class, PROPERTIES_FILE_NAME);
		readConfiguration();
	}

	public String retrievePersistenceFileName() {
		return getProperty(ControllerProperty.PERSISTENCE_FILE).getValue();
	}

	public String retrieveXmlDirectoryPath() {
		return getProperty(ControllerProperty.XML_DIRECTORY_PATH).getValue();
	}
	
	public Integer retrieveTradesSiteRetrieverThreadPoolSize() {
		ControllerProperty property = getProperty(ControllerProperty.TRADES_SITE_RETRIEVER_THREAD_POOL_SIZE);
		return Integer.parseInt(property.getValue());
	}
	
	public Duration retrieveTradesSiteRetrieverDurationStep() {
		ControllerProperty property = getProperty(ControllerProperty.TRADES_SITE_RETRIEVER_DURATION_STEP);
		int durationStep = Integer.parseInt(property.getValue());
		return Duration.ofMinutes(durationStep);
=======
import org.marceloleite.mercado.commons.properties.AbstractEnumPropertiesReader;

public class ControllerPropertiesRetriever extends AbstractEnumPropertiesReader<ControllerProperty> {

	private static final String PROPERTIES_FILE_NAME = "controller.properties";

	public ControllerPropertiesRetriever() {
		super(ControllerProperty.class, PROPERTIES_FILE_NAME);
		readConfiguration();
	}

	public String retrievePersistenceFileName() {
		return getProperty(ControllerProperty.PERSISTENCE_FILE).getValue();
	}

	public String retrieveXmlDirectoryPath() {
		return getProperty(ControllerProperty.XML_DIRECTORY_PATH).getValue();
>>>>>>> branch 'develop_1' of git@github.com:MarceloLeite2604/mercado.git
	}

}
