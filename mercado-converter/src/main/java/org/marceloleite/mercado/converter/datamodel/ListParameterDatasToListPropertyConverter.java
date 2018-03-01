package org.marceloleite.mercado.converter.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.base.model.data.ParameterData;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.properties.Property;
import org.marceloleite.mercado.properties.StandardProperty;

public class ListParameterDatasToListPropertyConverter implements Converter<List<ParameterData>, List<Property>>{

	@Override
	public List<Property> convertTo(List<ParameterData> parameterDatas) {
		List<Property> properties = new ArrayList<>();
		for (ParameterData parameterData : parameterDatas) {
			properties.add(new StandardProperty(parameterData.getName(), parameterData.getValue(), true));
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
		}
		return parameterDatas;
	}

}
