package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.base.model.data.ClassData;
import org.marceloleite.mercado.base.model.data.ParameterData;
import org.marceloleite.mercado.base.model.data.StrategyData;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.ParameterIdPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.ParameterPO;

public class ParameterPOToParameterDataConverter
		implements Converter<ParameterPO, ParameterData> {

	@Override
	public ParameterData convertTo(ParameterPO parameterPO) {
		ParameterData strategyParameterData = new ParameterData();
		strategyParameterData.setName(parameterPO.getId().getName());
		strategyParameterData.setValue(parameterPO.getValue());
		return strategyParameterData;
	}

	@Override
	public ParameterPO convertFrom(ParameterData parameterData) {
		ParameterPO parameterPO = new ParameterPO();
		ParameterIdPO parameterIdPO = new ParameterIdPO();
		ClassData classData = parameterData.getClassData();
		StrategyData strategyData = classData.getStrategyData();
		parameterIdPO.setClassStraAccoOwner(strategyData.getAccountData().getOwner());
		parameterIdPO.setClassStraCurrency(strategyData.getCurrency());
		parameterIdPO.setClassName(classData.getClassName());
		parameterIdPO.setName(parameterData.getName());
		parameterPO.setId(parameterIdPO);
		parameterPO.setValue(parameterData.getValue());
		return parameterPO;
	}

}
