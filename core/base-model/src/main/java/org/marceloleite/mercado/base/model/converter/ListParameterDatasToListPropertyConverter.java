package org.marceloleite.mercado.base.model.converter;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.commons.properties.StandardProperty;
import org.marceloleite.mercado.data.ParameterData;

public class ListParameterDatasToListPropertyConverter implements Converter<List<ParameterData>, List<Property>> {

	@Override
	public List<Property> convertTo(List<ParameterData> parameterDatas) {
		List<Property> properties = new ArrayList<>();
		if (parameterDatas != null && !parameterDatas.isEmpty()) {
			for (ParameterData parameterData : parameterDatas) {
				properties.add(new StandardProperty(parameterData.getName(), parameterData.getValue(), true));
			}
		}
		return properties;
	}

	@Override
	public List<ParameterData> convertFrom(List<Property> properties) {
		List<ParameterData> parameterDatas = new ArrayList<>();
		for (Property property : properties) {
			ParameterData parameterData = new ParameterData();
			parameterData.setName(property.getName());
			parameterData.setValue(property.getValue());
			parameterDatas.add(parameterData);
		}
		return parameterDatas;
	}

}
