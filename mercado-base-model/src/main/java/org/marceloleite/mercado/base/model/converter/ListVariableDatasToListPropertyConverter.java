package org.marceloleite.mercado.base.model.converter;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.commons.properties.StandardProperty;
import org.marceloleite.mercado.data.VariableData;

public class ListVariableDatasToListPropertyConverter implements Converter<List<VariableData>, List<Property>> {

	@Override
	public List<Property> convertTo(List<VariableData> variableDatas) {
		List<Property> properties = new ArrayList<>();
		if (variableDatas != null && !variableDatas.isEmpty()) {
			for (VariableData variableData : variableDatas) {
				properties.add(new StandardProperty(variableData.getName(), variableData.getValue(), true));
			}
		}
		return properties;
	}

	@Override
	public List<VariableData> convertFrom(List<Property> properties) {
		List<VariableData> variableDatas = new ArrayList<>();
		for (Property property : properties) {
			VariableData variableData = new VariableData();
			variableData.setName(property.getName());
			variableData.setValue(property.getValue());
			variableDatas.add(variableData);
		}
		return variableDatas;
	}

}
