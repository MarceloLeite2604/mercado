package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.database.data.structure.StrategyDataModel;
import org.marceloleite.mercado.database.data.structure.StrategyVariableDataModel;
import org.marceloleite.mercado.databasemodel.StrategyVariableIdPO;
import org.marceloleite.mercado.databasemodel.StrategyVariablePO;

public class StrategyVariablePOToStrategyVariableDataModelConverter
		implements Converter<StrategyVariablePO, StrategyVariableDataModel> {

	@Override
	public StrategyVariableDataModel convertTo(StrategyVariablePO strategyVariablePO) {
		StrategyVariableDataModel strategyVariableDataModel = new StrategyVariableDataModel();
		strategyVariableDataModel.setName(strategyVariablePO.getId().getVariableName());
		strategyVariableDataModel.setValue(strategyVariablePO.getValue());
		return strategyVariableDataModel;
	}

	@Override
	public StrategyVariablePO convertFrom(StrategyVariableDataModel strategyVariableDataModel) {
		StrategyVariablePO strategyVariablePO = new StrategyVariablePO();
		StrategyVariableIdPO strategyVariableIdPO = new StrategyVariableIdPO();
		StrategyDataModel strategyDataModel = strategyVariableDataModel.getStrategyDataModel();
		strategyVariableIdPO.setAccountOwner(strategyDataModel.getAccountDataModel().getOwner());
		strategyVariableIdPO.setStrategyName(strategyDataModel.getName());
		strategyVariableIdPO.setVariableName(strategyVariableDataModel.getName());
		strategyVariablePO.setStrategyVariableIdPO(strategyVariableIdPO);
		strategyVariablePO.setValue(strategyVariableDataModel.getValue());
		return strategyVariablePO;
	}

}
