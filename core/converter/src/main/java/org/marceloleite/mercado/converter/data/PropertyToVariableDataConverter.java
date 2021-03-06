package org.marceloleite.mercado.converter.data;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.commons.properties.StandardProperty;
import org.marceloleite.mercado.data.VariableData;

public class PropertyToVariableDataConverter implements Converter <Property, VariableData> {

	@Override
	public VariableData convertTo(Property property) {
		VariableData variableData = new VariableData();
		variableData.setName(property.getName());
		variableData.setValue(property.getValue());
		return variableData;
	}

	@Override
	public Property convertFrom(VariableData variableData) {
		String name = variableData.getName();
		String value = variableData.getValue();
		return new StandardProperty(name, value, false);
	}

}
