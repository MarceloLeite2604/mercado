package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.base.model.data.StrategyData;
import org.marceloleite.mercado.base.model.data.StrategyParameterData;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.StrategyParameterIdPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.StrategyParameterPO;

public class StrategyParameterPOToStrategyParameterDataConverter
		implements Converter<StrategyParameterPO, StrategyParameterData> {

	@Override
	public StrategyParameterData convertTo(StrategyParameterPO strategyParameterPO) {
		StrategyParameterData strategyParameterData = new StrategyParameterData();
		strategyParameterData.setName(strategyParameterPO.getId().getName());
		strategyParameterData.setValue(strategyParameterPO.getValue());
		return strategyParameterData;
	}

	@Override
	public StrategyParameterPO convertFrom(StrategyParameterData strategyParameterData) {
		StrategyParameterPO strategyParameterPO = new StrategyParameterPO();
		StrategyParameterIdPO strategyParameterIdPO = new StrategyParameterIdPO();
		StrategyData strategyData = strategyParameterData.getStrategyData();
		strategyParameterIdPO.setAccountOwner(strategyData.getAccountData().getOwner());
		strategyParameterIdPO.setStrategyCurrency(strategyData.getCurrency());
		strategyParameterIdPO.setName(strategyParameterData.getName());
		strategyParameterPO.setStrategyParameterIdPO(strategyParameterIdPO);
		strategyParameterPO.setValue(strategyParameterData.getValue());
		return strategyParameterPO;
	}

}
