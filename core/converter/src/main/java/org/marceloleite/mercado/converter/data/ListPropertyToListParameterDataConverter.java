package org.marceloleite.mercado.converter.data;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.data.ParameterData;

public class ListPropertyToListParameterDataConverter
		implements Converter<List<Property>, List<ParameterData>> {

	@Override
	public List<ParameterData> convertTo(List<Property> properties) {
		PropertyToParameterDataConverter propertyToParameterDataConverter = new PropertyToParameterDataConverter();
		List<ParameterData> parameterDatas = new ArrayList<>();
		if (properties != null && !properties.isEmpty()) {
			for (Property property : properties) {
				ParameterData parameterData = propertyToParameterDataConverter.convertTo(property);
				parameterDatas.add(parameterData);
			}
		}
		return parameterDatas;
	}

	@Override
	public List<Property> convertFrom(List<ParameterData> parameterDatas) {
		PropertyToParameterDataConverter propertyToParameterDataConverter = new PropertyToParameterDataConverter();
		List<Property> properties = new ArrayList<>();
		if (parameterDatas != null && parameterDatas.isEmpty()) {
			for (ParameterData parameterData : parameterDatas) {
				Property property = propertyToParameterDataConverter.convertFrom(parameterData);
				properties.add(property);
			}
		}
		return properties;
	}

}
