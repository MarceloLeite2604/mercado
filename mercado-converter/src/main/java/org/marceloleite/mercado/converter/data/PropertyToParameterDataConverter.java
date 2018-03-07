package org.marceloleite.mercado.converter.data;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.commons.properties.StandardProperty;
import org.marceloleite.mercado.data.ParameterData;

public class PropertyToParameterDataConverter implements Converter <Property, ParameterData> {

	@Override
	public ParameterData convertTo(Property property) {
		ParameterData parameterData = new ParameterData();
		parameterData.setName(property.getName());
		parameterData.setValue(property.getValue());
		return parameterData;
	}

	@Override
	public Property convertFrom(ParameterData parameterData) {
		Property property = new StandardProperty();
		property.setName(parameterData.getName());
		property.setValue(parameterData.getValue());
		
		return property;
	}

}
