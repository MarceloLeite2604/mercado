package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.database.data.structure.PropertyDataModel;
import org.marceloleite.mercado.databasemodel.PropertyPO;

public class PropertyPOToPropertyDataModelConverter implements Converter<PropertyPO, PropertyDataModel>{

	@Override
	public PropertyDataModel convertTo(PropertyPO propertyPO) {
		PropertyDataModel propertyDataModel = new PropertyDataModel();
		propertyDataModel.setName(propertyPO.getName());
		propertyDataModel.setValue(propertyPO.getValue());
		return propertyDataModel;
	}

	@Override
	public PropertyPO convertFrom(PropertyDataModel propertyDataModel) {
		PropertyPO propertyPO = new PropertyPO();
		propertyPO.setName(propertyDataModel.getName());
		propertyPO.setValue(propertyDataModel.getValue());
		return propertyPO;
	}

}
