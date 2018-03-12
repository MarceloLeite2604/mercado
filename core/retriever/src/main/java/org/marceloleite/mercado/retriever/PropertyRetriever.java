package org.marceloleite.mercado.retriever;

import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.commons.properties.StandardPropertiesReader;
import org.marceloleite.mercado.commons.properties.StandardProperty;
import org.marceloleite.mercado.databaseretriever.persistence.daos.PropertyDAO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PropertyPO;
import org.marceloleite.mercado.retriever.converter.PropertyPOToStandardPropertyConverter;
import org.marceloleite.mercado.retriever.converter.StandardPropertyToPropertyPOConverter;

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
				standardProperty = new PropertyPOToStandardPropertyConverter().convertTo(propertyPO);
			}
		}
		if (ignoreDatabaseValue || (!ignoreDatabaseValue && !propertyPoIsValid(propertyPO))) {
			standardProperty = retrievePropertyFromConfigurationFile(property.getName());
			if (standardProperty != null && standardProperty.getValue() != null) {
				PropertyPO propertyPoToPersist = new StandardPropertyToPropertyPOConverter().convertTo(standardProperty);
				propertyDAO.merge(propertyPoToPersist);
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
		}
	}
}
