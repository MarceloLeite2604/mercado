package org.marceloleite.mercado.converter.entity;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.VariableData;
import org.marceloleite.mercado.databaseretriever.persistence.objects.VariablePO;

public class ListVariablePOToListVariableDataConverter implements Converter<List<VariablePO>, List<VariableData>> {

	@Override
	public List<VariableData> convertTo(List<VariablePO> variablePOs) {
		VariablePOToVariableDataConverter variablePOToVariableDataConverter = new VariablePOToVariableDataConverter();
		List<VariableData> variableDatas = new ArrayList<>();
		for (VariablePO variablePO : variablePOs) {
			variableDatas.add(variablePOToVariableDataConverter.convertTo(variablePO));
		}
		return variableDatas;
	}

	@Override
	public List<VariablePO> convertFrom(List<VariableData> variableDatas) {
		VariablePOToVariableDataConverter variablePOToVariableDataConver = new VariablePOToVariableDataConverter();
		List<VariablePO> variablePOs = new ArrayList<>();
		if (variableDatas != null && !variableDatas.isEmpty()) {
			for (VariableData variableData : variableDatas) {
				variablePOs.add(variablePOToVariableDataConver.convertFrom(variableData));
			}
		}
		return variablePOs;
	}

}
