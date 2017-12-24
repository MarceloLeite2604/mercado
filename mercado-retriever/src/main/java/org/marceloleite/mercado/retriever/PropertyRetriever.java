package org.marceloleite.mercado.retriever;

import org.marceloleite.mercado.databasemodel.PropertyPO;
import org.marceloleite.mercado.databaseretriever.PropertyDatabaseRetriever;
import org.marceloleite.mercado.databaseretriever.persistence.dao.PropertyDAO;
import org.marceloleite.mercado.properties.StandardPropertiesReader;
import org.marceloleite.mercado.properties.StandardProperty;

public class PropertyRetriever {

	private String configurationFilePath;

	private StandardPropertiesReader standardConfiguration;

	public PropertyRetriever(String configurationFilePath) {
		super();
		this.configurationFilePath = configurationFilePath;
	}

	public PropertyPO retrieve(String name, boolean ignoreDatabaseValue) {
		PropertyPO property = null;
		PropertyPO propertyPO = null;
		PropertyPO configurationFileRetrievedProperty = null;
		if (!ignoreDatabaseValue) {
			PropertyPO propertyPO = new PropertyPO();
			property.setName(name);
			propertyPO = new PropertyDAO().findById(property);
		}
		if (ignoreDatabaseValue || (!ignoreDatabaseValue && propertyPO == null)) {
			configurationFileRetrievedProperty = retrievePropertyFromConfigurationFile(name);
		}

		if (propertyPO != null) {
			property = propertyPO;
		} else {
			property = configurationFileRetrievedProperty;
		}

		return property;
	}

	private PropertyPO retrievePropertyFromConfigurationFile(String name) {
		PropertyPO property = null;
		String configurationFileRetrievedProperty;

		createStandardConfiguration();
		configurationFileRetrievedProperty = standardConfiguration.getProperty(new StandardProperty(name, false));

		if (configurationFileRetrievedProperty != null) {
			property = new PropertyPO();
			property.setName(name);
			property.setValue(configurationFileRetrievedProperty);
		}

		return property;
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
