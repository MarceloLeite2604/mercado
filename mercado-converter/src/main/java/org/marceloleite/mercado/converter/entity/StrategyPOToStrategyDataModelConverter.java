package org.marceloleite.mercado.converter.entity;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.database.data.structure.StrategyDataModel;
import org.marceloleite.mercado.database.data.structure.StrategyParameterDataModel;
import org.marceloleite.mercado.database.data.structure.StrategyVariableDataModel;
import org.marceloleite.mercado.databasemodel.StrategyIdPO;
import org.marceloleite.mercado.databasemodel.StrategyPO;
import org.marceloleite.mercado.databasemodel.StrategyParameterPO;
import org.marceloleite.mercado.databasemodel.StrategyVariablePO;

public class StrategyPOToStrategyDataModelConverter implements Converter<StrategyPO, StrategyDataModel> {

	@Override
	public StrategyDataModel convertTo(StrategyPO strategyPO) {
		StrategyDataModel strategyDataModel = new StrategyDataModel();
		strategyDataModel.getParameters().addAll(createStrategyParameterDataModelList(strategyPO));
		strategyDataModel.getVariables().addAll(createStrategyVariableDataModelList(strategyPO));
		strategyDataModel.setName(strategyPO.getName());
		strategyDataModel.setClassName(strategyPO.getClassName());

		return strategyDataModel;
	}
	
	@Override
	public StrategyPO convertFrom(StrategyDataModel strategyDataModel) {
		StrategyPO strategyPO = new StrategyPO();
		StrategyIdPO strategyIdPO = new StrategyIdPO();
		strategyIdPO.setAccountOwner(strategyDataModel.getAccountDataModel().getOwner());
		strategyIdPO.setName(strategyDataModel.getName());
		strategyPO.setStrategyIdPO(strategyIdPO);
		strategyPO.setName(strategyDataModel.getName());
		strategyPO.setClassName(strategyDataModel.getClassName());
		
		strategyPO.setStrategyParameterPOs(createStrategyParameterPOList(strategyDataModel));
		strategyPO.setStrategyVariablePOs(createStrategyVariablePOList(strategyDataModel));
		
		return strategyPO;
	}

	private List<StrategyVariablePO> createStrategyVariablePOList(StrategyDataModel strategyDataModel) {
		List<StrategyVariableDataModel> strategyVariableDataModels = strategyDataModel.getVariables();
		StrategyVariablePOToStrategyVariableDataModelConverter strategyVariablePOToStrategyVariableDataModelConverter = new StrategyVariablePOToStrategyVariableDataModelConverter();
		List<StrategyVariablePO> strategyVariablePOs = new ArrayList<>();
		if ( strategyVariableDataModels != null && !strategyVariableDataModels.isEmpty()) {
			for (StrategyVariableDataModel strategyVariableDataModel : strategyVariableDataModels) {
				StrategyVariablePO strategyVariablePO = strategyVariablePOToStrategyVariableDataModelConverter.convertFrom(strategyVariableDataModel);
				strategyVariablePOs.add(strategyVariablePO);
			}
		}
		return strategyVariablePOs;
	}

	private List<StrategyParameterPO> createStrategyParameterPOList(StrategyDataModel strategyDataModel) {
		List<StrategyParameterDataModel> strategyParameterDataModels = strategyDataModel.getParameters();
		StrategyParameterPOToStrategyParameterDataModelConverter strategyPropertyPOToStrategyPropertyDataModelConverter = new StrategyParameterPOToStrategyParameterDataModelConverter();
		List<StrategyParameterPO> strategyParameterPOs = new ArrayList<>();
		if ( strategyParameterDataModels != null && !strategyParameterDataModels.isEmpty()) {
			for (StrategyParameterDataModel strategyParameterDataModel : strategyParameterDataModels) {
				StrategyParameterPO strategyParameterPO = strategyPropertyPOToStrategyPropertyDataModelConverter.convertFrom(strategyParameterDataModel);
				strategyParameterPOs.add(strategyParameterPO);
			}
		}
		return strategyParameterPOs;
	}

	private List<StrategyVariableDataModel> createStrategyVariableDataModelList(StrategyPO strategyPO) {
		List<StrategyVariablePO> strategyVariablePOs = strategyPO.getStrategyVariablePOs();
		
		List<StrategyVariableDataModel> variables = new ArrayList<>();
		
		if (strategyVariablePOs != null && !strategyVariablePOs.isEmpty()) {
			for (StrategyVariablePO strategyVariablePO : strategyVariablePOs) {
				StrategyVariableDataModel variable = new StrategyVariableDataModel();
				variable.setName(strategyVariablePO.getId().getVariableName());
				variable.setValue(strategyVariablePO.getValue());
				variables.add(variable);
			}
		}
		return variables;
	}

	private List<StrategyParameterDataModel> createStrategyParameterDataModelList(StrategyPO strategyPO) {
		List<StrategyParameterPO> strategyPropertyPOs = strategyPO.getStrategyParameterPOs();

		List<StrategyParameterDataModel> parameters = new ArrayList<>();
		
		if (strategyPropertyPOs != null && !strategyPropertyPOs.isEmpty()) {
			for (StrategyParameterPO strategyParameterPO : strategyPropertyPOs) {
				StrategyParameterDataModel parameter = new StrategyParameterDataModel();
				parameter.setName(strategyParameterPO.getId().getParameterName());
				parameter.setValue(strategyParameterPO.getValue());
				parameters.add(parameter);
			}
		}
		return parameters;
	}

	

}
