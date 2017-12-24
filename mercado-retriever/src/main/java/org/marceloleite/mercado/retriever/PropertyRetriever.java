package org.marceloleite.mercado.retriever;

import org.marceloleite.mercado.databasemodel.PropertyPO;
import org.marceloleite.mercado.databaseretriever.persistence.dao.PropertyDAO;
import org.marceloleite.mercado.properties.Property;
import org.marceloleite.mercado.properties.StandardPropertiesReader;
import org.marceloleite.mercado.properties.StandardProperty;

public class PropertyRetriever {

	private String configurationFilePath;

	private StandardPropertiesReader standardConfiguration;

	public PropertyRetriever(String configurationFilePath) {
		super();
		this.configurationFilePath = configurationFilePath;
	}

	public StandardProperty retrieve(String name, boolean ignoreDatabaseValue) {
		StandardProperty standardProperty = null;
		PropertyPO propertyPO = null;
		if (!ignoreDatabaseValue) {
			PropertyPO propertyPOforEnquirement = new PropertyPO();
			propertyPOforEnquirement.setName(name);
			propertyPO = new PropertyDAO().findById(propertyPOforEnquirement);
			standardProperty = new PropertyPOToStandardPropertyConverter().convert(propertyPO);
		}
		if (ignoreDatabaseValue || (!ignoreDatabaseValue && propertyPO == null)) {
			standardProperty = retrievePropertyFromConfigurationFile(name);
		}

		return standardProperty;
	}

	private StandardProperty retrievePropertyFromConfigurationFile(String name) {
		StandardProperty propertyForEnquirement = new StandardProperty(name, false);

		createStandardConfiguration();
		Property property = standardConfiguration.getProperty(propertyForEnquirement);

		return new StandardProperty(property.getName(), property.getValue(), property.isRequired());
	}

	private void createStandardConfiguration() {
		if (standardConfiguration == null) {
			if (configurationFilePath == null) {
				standardConfiguration = new StandardPropertiesReader();
			} else {
				standardConfiguration = new StandardPropertiesReader(configurationFilePath);
			}
			standardConfiguration.readConfiguration();
		}
	}
}
