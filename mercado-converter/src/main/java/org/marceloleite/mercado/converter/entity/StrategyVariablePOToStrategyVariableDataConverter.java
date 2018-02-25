package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.base.model.data.StrategyData;
import org.marceloleite.mercado.base.model.data.StrategyVariableData;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databasemodel.StrategyVariableIdPO;
import org.marceloleite.mercado.databasemodel.StrategyVariablePO;

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
		StrategyVariableIdPO strategyVariableIdPO = new StrategyVariableIdPO();
		StrategyData strategyData = strategyVariableData.getStrategyData();
		strategyVariableIdPO.setAccountOwner(strategyData.getAccountData().getOwner());
		strategyVariableIdPO.setName(strategyVariableData.getName());
		strategyVariablePO.setStrategyVariableIdPO(strategyVariableIdPO);
		strategyVariablePO.setValue(strategyVariableData.getValue());
		return strategyVariablePO;
	}

}
