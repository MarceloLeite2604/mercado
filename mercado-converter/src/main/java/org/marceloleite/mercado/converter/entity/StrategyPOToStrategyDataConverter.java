package org.marceloleite.mercado.converter.entity;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.data.ClassData;
import org.marceloleite.mercado.data.StrategyData;
import org.marceloleite.mercado.data.VariableData;
import org.marceloleite.mercado.databaseretriever.persistence.objects.ClassIdPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.ClassPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.ParameterPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.StrategyIdPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.StrategyPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.VariablePO;

public class StrategyPOToStrategyDataConverter implements Converter<StrategyPO, StrategyData> {

	@Override
	public StrategyData convertTo(StrategyPO strategyPO) {
		StrategyData strategyData = new StrategyData();
		strategyData.setClassDatas(createStrategyClassDatas(strategyPO));
		strategyData.setCurrency(strategyPO.getId().getCurrency());
		return strategyData;
	}

	@Override
	public StrategyPO convertFrom(StrategyData strategyData) {
		StrategyPO strategyPO = new StrategyPO();
		StrategyIdPO strategyIdPO = new StrategyIdPO();
		strategyIdPO.setAccoOwner(strategyData.getAccountData().getOwner());
		strategyPO.setId(strategyIdPO);
		List<ClassPO> classPOs = createClassPOs(strategyData);
		classPOs.forEach(classPO -> classPO.setStrategyPO(strategyPO));
		strategyPO.setClassPOs(classPOs);
		return strategyPO;
	}

	private List<ClassPO> createClassPOs(StrategyData strategyData) {
		List<ClassData> classDatas = strategyData.getClassDatas();
		List<ClassPO> classPOs = new ArrayList<>();
		ListParameterPOToListParameterDataConverter listParameterPOToListParameterDataConverter = new ListParameterPOToListParameterDataConverter();
		ListVariablePOToListVariableDataConverter listVariablePOToListVariableDataConverter = new ListVariablePOToListVariableDataConverter();

		if (classDatas != null && !classDatas.isEmpty()) {
			for (ClassData classData : classDatas) {
				ClassPO classPO = new ClassPO();
				ClassIdPO classIdPO = new ClassIdPO();
				classIdPO.setStraAccoOwner(strategyData.getAccountData().getOwner());
				classIdPO.setStraCurrency(strategyData.getCurrency());
				classIdPO.setName(classData.getName());
				classPO.setId(classIdPO);
				
				List<ParameterPO> parameterPOs = listParameterPOToListParameterDataConverter.convertFrom(classData.getParameterDatas());
				parameterPOs.forEach(parameterPO -> parameterPO.setClassPO(classPO));
				classPO.setParameterPOs(parameterPOs);
				
				List<VariableData> variableDatas = classData.getVariableDatas();
				List<VariablePO> variablePOs = listVariablePOToListVariableDataConverter.convertFrom(variableDatas);
				parameterPOs.forEach(variablePO -> variablePO.setClassPO(classPO));
				classPO.setVariablePOs(variablePOs);
				
				classPOs.add(classPO);
			}
		}
		return classPOs;
	}

	private List<ClassData> createStrategyClassDatas(StrategyPO strategyPO) {
		List<ClassPO> classPOs = strategyPO.getClassPOs();
		List<ClassData> classDatas = new ArrayList<>();

		if (classPOs != null && !classPOs.isEmpty()) {
			for (ClassPO classPO : classPOs) {
				ClassData classData = new ClassData();
				classData.setName(classPO.getId().getName());
				classDatas.add(classData);
			}
		}
		return classDatas;
	}

}
