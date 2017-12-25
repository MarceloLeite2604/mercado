package org.marceloleite.mercado.retriever;

import org.marceloleite.mercado.databasemodel.PropertyPO;
import org.marceloleite.mercado.databaseretriever.persistence.dao.PropertyDAO;
import org.marceloleite.mercado.properties.Property;
import org.marceloleite.mercado.properties.StandardPropertiesReader;
import org.marceloleite.mercado.properties.StandardProperty;

public class PropertyRetriever {

	private String configurationFilePath;

	private StandardPropertiesReader standardConfiguration;

	private PropertyDAO propertyDAO;

	public PropertyRetriever() {
		this(null);
	}

	public PropertyRetriever(String configurationFilePath) {
		super();
		this.configurationFilePath = configurationFilePath;
		this.propertyDAO = new PropertyDAO();
	}

	public StandardProperty retrieve(Property property, boolean ignoreDatabaseValue) {
		StandardProperty standardProperty = null;
		PropertyPO propertyPO = null;
		if (!ignoreDatabaseValue) {
			PropertyPO propertyPOforEnquirement = new PropertyPO();
			propertyPOforEnquirement.setName(property.getName());
			propertyPO = propertyDAO.findById(propertyPOforEnquirement);
			if (propertyPoIsValid(propertyPO)) {
				standardProperty = new PropertyPOToStandardPropertyConverter().convert(propertyPO);
			}
		}
		if (ignoreDatabaseValue || (!ignoreDatabaseValue && !propertyPoIsValid(propertyPO))) {
			standardProperty = retrievePropertyFromConfigurationFile(property.getName());
			if (standardProperty != null && standardProperty.getValue() != null) {
				PropertyPO propertyPoToPersist = new StandardPropertyToPropertyPOConverter().convert(standardProperty);
				propertyDAO.persist(propertyPoToPersist);
			}
		}

		return standardProperty;
	}

	private boolean propertyPoIsValid(PropertyPO propertyPO) {
		return (propertyPO != null && propertyPO.getValue() != null && !propertyPO.getValue().isEmpty());
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
