package org.marceloleite.mercado.controller.properties;

import org.marceloleite.mercado.commons.encryption.Encrypt;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.commons.properties.StandardProperty;
import org.marceloleite.mercado.retriever.PropertyRetriever;

public class ControllerPropertiesRetriever {

	private static final boolean IGNORE_DATABASE_VALUES = true;

	private static final String PROPERTIES_FILE_NAME = "controller.properties";

	private PropertyRetriever propertyRetriever;

	public ControllerPropertiesRetriever() {
		this.propertyRetriever = new PropertyRetriever(PROPERTIES_FILE_NAME);
	}

	private Property retrieveProperty(ControllerProperty controllerProperty) {
		StandardProperty standardProperty = propertyRetriever.retrieve(controllerProperty, IGNORE_DATABASE_VALUES);
		if (standardProperty == null) {
			if (controllerProperty.isRequired()) {
				throw new RuntimeException("Could not find property \"" + controllerProperty.getName() + "\".");
			} else {
				return new StandardProperty(controllerProperty.getName(), controllerProperty.getDefaultValue(),
						controllerProperty.isRequired());
			}

		} else {
			if (controllerProperty.isEncrypted()) {
				String decryptedValue = new Encrypt().decrypt(standardProperty.getValue());
				return new StandardProperty(controllerProperty.getName(), decryptedValue,
						controllerProperty.isRequired());
			} else {
				return standardProperty;
			}
		}

	}

	public String retrievePersistenceFileName() {
		return retrieveProperty(ControllerProperty.PERSISTENCE_FILE).getValue();
	}
	
	public String retrieveXmlDirectoryPath() {
		return retrieveProperty(ControllerProperty.XML_DIRECTORY_PATH).getValue();
	}

}
