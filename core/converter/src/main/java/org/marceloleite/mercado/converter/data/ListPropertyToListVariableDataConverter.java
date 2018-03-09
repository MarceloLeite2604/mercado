package org.marceloleite.mercado.converter.data;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.data.VariableData;

public class ListPropertyToListVariableDataConverter
		implements Converter<List<Property>, List<VariableData>> {

	@Override
	public List<VariableData> convertTo(List<Property> properties) {
		PropertyToVariableDataConverter propertyToVariableDataConverter = new PropertyToVariableDataConverter();
		List<VariableData> variableDatas = new ArrayList<>();
		if (properties != null && !properties.isEmpty()) {
			for (Property property : properties) {
				VariableData variableData = propertyToVariableDataConverter.convertTo(property);
				variableDatas.add(variableData);
			}
		}
		return variableDatas;
	}

	@Override
	public List<Property> convertFrom(List<VariableData> variableDatas) {
		PropertyToVariableDataConverter propertyToVariableDataConverter = new PropertyToVariableDataConverter();
		List<Property> properties = new ArrayList<>();
		if (variableDatas != null && variableDatas.isEmpty()) {
			for (VariableData variableData : variableDatas) {
				Property property = propertyToVariableDataConverter.convertFrom(variableData);
				properties.add(property);
			}
		}
		return properties;
	}

}
