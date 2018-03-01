package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.base.model.data.ClassData;
import org.marceloleite.mercado.base.model.data.StrategyData;
import org.marceloleite.mercado.base.model.data.VariableData;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.VariableIdPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.VariablePO;

public class VariablePOToVariableDataConverter
		implements Converter<VariablePO, VariableData> {

	@Override
	public VariableData convertTo(VariablePO variablePO) {
		VariableData variableData = new VariableData();
		variableData.setValue(variablePO.getValue());
		return variableData;
	}

	@Override
	public VariablePO convertFrom(VariableData variableData) {
		VariablePO variablePO = new VariablePO();
		ClassData classData = variableData.getClassData();
		StrategyData strategyData = classData.getStrategyData();
		VariableIdPO variableIdPO = new VariableIdPO();
		variableIdPO.setClassStraAccoOwner(strategyData.getAccountData().getOwner());
		variableIdPO.setClassStraCurrency(strategyData.getCurrency());
		variableIdPO.setClassName(classData.getName());
		variableIdPO.setName(variableData.getName());
		variablePO.setId(variableIdPO);
		variablePO.setValue(variableData.getValue());
		return variablePO;
	}

}
