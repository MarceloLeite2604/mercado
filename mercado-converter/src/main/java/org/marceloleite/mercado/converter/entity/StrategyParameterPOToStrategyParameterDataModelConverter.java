package org.marceloleite.mercado.converter.entity;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.database.data.structure.StrategyDataModel;
import org.marceloleite.mercado.database.data.structure.StrategyParameterDataModel;
import org.marceloleite.mercado.databasemodel.StrategyParameterIdPO;
import org.marceloleite.mercado.databasemodel.StrategyParameterPO;

public class StrategyParameterPOToStrategyParameterDataModelConverter
		implements Converter<StrategyParameterPO, StrategyParameterDataModel> {

	@Override
	public StrategyParameterDataModel convertTo(StrategyParameterPO strategyParameterPO) {
		StrategyParameterDataModel strategyParameterDataModel = new StrategyParameterDataModel();
		strategyParameterDataModel.setName(strategyParameterPO.getId().getParameterName());
		strategyParameterDataModel.setValue(strategyParameterPO.getValue());
		return strategyParameterDataModel;
	}

	@Override
	public StrategyParameterPO convertFrom(StrategyParameterDataModel strategyParameterDataModel) {
		StrategyParameterPO strategyParameterPO = new StrategyParameterPO();
		StrategyParameterIdPO strategyParameterIdPO = new StrategyParameterIdPO();
		StrategyDataModel strategyDataModel = strategyParameterDataModel.getStrategyDataModel();
		strategyParameterIdPO.setAccountOwner(strategyDataModel.getAccountDataModel().getOwner());
		strategyParameterIdPO.setStrategyName(strategyDataModel.getName());
		strategyParameterIdPO.setParameterName(strategyParameterDataModel.getName());
		strategyParameterPO.setStrategyParameterIdPO(strategyParameterIdPO);
		strategyParameterPO.setValue(strategyParameterDataModel.getValue());
		return strategyParameterPO;
	}

}
