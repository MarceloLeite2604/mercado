package org.marceloleite.mercado.converter.entity;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.base.model.data.StrategyClassData;
import org.marceloleite.mercado.base.model.data.StrategyData;
import org.marceloleite.mercado.base.model.data.StrategyParameterData;
import org.marceloleite.mercado.base.model.data.StrategyVariableData;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databasemodel.StrategyClassIdPO;
import org.marceloleite.mercado.databasemodel.StrategyClassPO;
import org.marceloleite.mercado.databasemodel.StrategyIdPO;
import org.marceloleite.mercado.databasemodel.StrategyPO;
import org.marceloleite.mercado.databasemodel.StrategyParameterPO;
import org.marceloleite.mercado.databasemodel.StrategyVariablePO;

public class StrategyPOToStrategyDataConverter implements Converter<StrategyPO, StrategyData> {

	@Override
	public StrategyData convertTo(StrategyPO strategyPO) {
		StrategyData strategyData = new StrategyData();
		List<StrategyParameterData> strategyParameterDatas = createStrategyParameterDatas(strategyPO);
		strategyData.setStrategyParameterDatas(strategyParameterDatas);
		List<StrategyVariableData> strategyVariableDatas = createStrategyVariableDatas(strategyPO);
		strategyData.setStrategyVariableDatas(strategyVariableDatas);
		List<StrategyClassData> strategyClassDatas = createStrategyClassDatas(strategyPO);
		strategyData.setStrategyClassDatas(strategyClassDatas);
		strategyData.setCurrency(strategyPO.getCurrency());
		return strategyData;
	}

	@Override
	public StrategyPO convertFrom(StrategyData strategyData) {
		StrategyPO strategyPO = new StrategyPO();
		StrategyIdPO strategyIdPO = new StrategyIdPO();
		strategyIdPO.setAccountOwner(strategyData.getAccountData().getOwner());
		strategyPO.setStrategyIdPO(strategyIdPO);
		strategyPO.setStratrategyClassPOs(createStrategyClassPOs(strategyData));
		strategyPO.setStrategyParameterPOs(createStrategyParameterPOList(strategyData));
		strategyPO.setStrategyVariablePOs(createStrategyVariablePOList(strategyData));

		return strategyPO;
	}

	private List<StrategyVariablePO> createStrategyVariablePOList(StrategyData strategyData) {
		List<StrategyVariableData> strategyVariableDatas = strategyData.getStrategyVariableDatas();
		StrategyVariablePOToStrategyVariableDataConverter strategyVariablePOToStrategyVariableDataConverter = new StrategyVariablePOToStrategyVariableDataConverter();
		List<StrategyVariablePO> strategyVariablePOs = new ArrayList<>();
		if (strategyVariableDatas != null && !strategyVariableDatas.isEmpty()) {
			for (StrategyVariableData strategyVariableData : strategyVariableDatas) {
				StrategyVariablePO strategyVariablePO = strategyVariablePOToStrategyVariableDataConverter
						.convertFrom(strategyVariableData);
				strategyVariablePOs.add(strategyVariablePO);
			}
		}
		return strategyVariablePOs;
	}

	private List<StrategyParameterPO> createStrategyParameterPOList(StrategyData strategyData) {
		List<StrategyParameterData> strategyParameterDatas = strategyData.getStrategyParameterDatas();
		StrategyParameterPOToStrategyParameterDataConverter strategyPropertyPOToStrategyPropertyDataConverter = new StrategyParameterPOToStrategyParameterDataConverter();
		List<StrategyParameterPO> strategyParameterPOs = new ArrayList<>();
		if (strategyParameterDatas != null && !strategyParameterDatas.isEmpty()) {
			for (StrategyParameterData strategyParameterData : strategyParameterDatas) {
				StrategyParameterPO strategyParameterPO = strategyPropertyPOToStrategyPropertyDataConverter
						.convertFrom(strategyParameterData);
				strategyParameterPOs.add(strategyParameterPO);
			}
		}
		return strategyParameterPOs;
	}
	
	private List<StrategyClassPO> createStrategyClassPOs(StrategyData strategyData) {
		List<StrategyClassData> stratrategyClassDatas = strategyData.getStrategyClassDatas();
		List<StrategyClassPO> strategyClassPOs = new ArrayList<>();

		if (stratrategyClassDatas != null && !stratrategyClassDatas.isEmpty()) {
			for (StrategyClassData strategyClassData : stratrategyClassDatas) {
				StrategyClassPO strategyClassPO = new StrategyClassPO();
				StrategyClassIdPO strategyClassIdPO = new StrategyClassIdPO();
				strategyClassIdPO.setAccountOwner(strategyData.getAccountData().getOwner());
				strategyClassIdPO.setClassName(strategyClassData.getClassName());
				strategyClassPO.setStrategyClassIdPO(strategyClassIdPO);
				strategyClassPOs.add(strategyClassPO);
			}
		}
		return strategyClassPOs;
	}

	private List<StrategyClassData> createStrategyClassDatas(StrategyPO strategyPO) {
		List<StrategyClassPO> stratrategyClassPOs = strategyPO.getStratrategyClassPOs();
		List<StrategyClassData> strategyClassDatas = new ArrayList<>();

		if (stratrategyClassPOs != null && !stratrategyClassPOs.isEmpty()) {
			for (StrategyClassPO strategyClassPO : stratrategyClassPOs) {
				StrategyClassData strategyClassData = new StrategyClassData();
				strategyClassData.setClassName(strategyClassPO.getId().getClassName());
				strategyClassDatas.add(strategyClassData);
			}
		}
		return strategyClassDatas;
	}

	private List<StrategyVariableData> createStrategyVariableDatas(StrategyPO strategyPO) {
		List<StrategyVariablePO> strategyVariablePOs = strategyPO.getStrategyVariablePOs();

		List<StrategyVariableData> strategyVariableDatas = new ArrayList<>();

		if (strategyVariablePOs != null && !strategyVariablePOs.isEmpty()) {
			for (StrategyVariablePO strategyVariablePO : strategyVariablePOs) {
				StrategyVariableData strategyVariableData = new StrategyVariableData();
				strategyVariableData.setValue(strategyVariablePO.getValue());
				strategyVariableDatas.add(strategyVariableData);
			}
		}
		return strategyVariableDatas;
	}

	private List<StrategyParameterData> createStrategyParameterDatas(StrategyPO strategyPO) {
		List<StrategyParameterPO> strategyPropertyPOs = strategyPO.getStrategyParameterPOs();

		List<StrategyParameterData> strategyParameterDatas = new ArrayList<>();

		if (strategyPropertyPOs != null && !strategyPropertyPOs.isEmpty()) {
			for (StrategyParameterPO strategyParameterPO : strategyPropertyPOs) {
				StrategyParameterData strategyParameterData = new StrategyParameterData();
				strategyParameterData.setValue(strategyParameterPO.getValue());
				strategyParameterDatas.add(strategyParameterData);
			}
		}
		return strategyParameterDatas;
	}

}
