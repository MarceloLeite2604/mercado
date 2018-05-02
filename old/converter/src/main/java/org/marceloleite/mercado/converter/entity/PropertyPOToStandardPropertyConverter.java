package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.commons.properties.StandardProperty;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PropertyPO;

public class PropertyPOToStandardPropertyConverter implements Converter<PropertyPO, StandardProperty> {

	@Override
	public StandardProperty convertTo(PropertyPO propertyPO) {
		String name = propertyPO.getName();
		String value = propertyPO.getValue();
		return new StandardProperty(name, value, false);
	}

	@Override
	public PropertyPO convertFrom(StandardProperty standardProperty) {
		PropertyPO propertyPO = new PropertyPO();
		propertyPO.setName(standardProperty.getName());
		propertyPO.setValue(standardProperty.getValue());
		return propertyPO;
	}

}
