package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.PropertyData;
import org.marceloleite.mercado.databaseretriever.persistence.objects.PropertyPO;

public class PropertyPOToPropertyConverter implements Converter<PropertyPO, PropertyData> {

	@Override
	public PropertyData convertTo(PropertyPO PropertyPO) {
		PropertyData propertyData = new PropertyData();
		propertyData.setName(PropertyPO.getName());
		propertyData.setValue(PropertyPO.getValue());
		return propertyData;
	}

	@Override
	public PropertyPO convertFrom(PropertyData propertyData) {
		PropertyPO propertyPO = new PropertyPO();
		propertyPO.setName(propertyData.getName());
		propertyPO.setValue(propertyData.getValue());
		return propertyPO;
	}

}
