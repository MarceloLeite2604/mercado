package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.base.model.data.StrategyData;
import org.marceloleite.mercado.base.model.data.StrategyVariableData;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.StrategyVariableIdPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.StrategyVariablePO;

public class StrategyVariablePOToStrategyVariableDataConverter
		implements Converter<StrategyVariablePO, StrategyVariableData> {

	@Override
	public StrategyVariableData convertTo(StrategyVariablePO strategyVariablePO) {
		StrategyVariableData strategyVariableData = new StrategyVariableData();
		strategyVariableData.setValue(strategyVariablePO.getValue());
		return strategyVariableData;
	}

	@Override
	public StrategyVariablePO convertFrom(StrategyVariableData strategyVariableData) {
		StrategyVariablePO strategyVariablePO = new StrategyVariablePO();
		StrategyData strategyData = strategyVariableData.getStrategyData();
		// StrategyIdPO strategyIdPO = new StrategyIdPO();
		// strategyIdPO.setAccountOwner(strategyData.getAccountData().getOwner());
		// strategyIdPO.setCurrency(strategyData.getCurrency());
		StrategyVariableIdPO strategyVariableIdPO = new StrategyVariableIdPO();
		// strategyVariableIdPO.setStrategyIdPO(strategyIdPO);
		strategyVariableIdPO.setAccountOwner(strategyData.getAccountData().getOwner());
		strategyVariableIdPO.setStrategyCurrency(strategyData.getCurrency());
		strategyVariableIdPO.setName(strategyVariableData.getName());
		strategyVariablePO.setStrategyVariableIdPO(strategyVariableIdPO);
		strategyVariablePO.setValue(strategyVariableData.getValue());
		return strategyVariablePO;
	}

}
