package org.marceloleite.mercado.converter.entity;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.base.model.data.ClassData;
import org.marceloleite.mercado.base.model.data.StrategyData;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.ClassIdPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.ClassPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.StrategyIdPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.StrategyPO;

public class StrategyPOToStrategyDataConverter implements Converter<StrategyPO, StrategyData> {

	@Override
	public StrategyData convertTo(StrategyPO strategyPO) {
		StrategyData strategyData = new StrategyData();
		List<ClassData> strategyClassDatas = createStrategyClassDatas(strategyPO);
		strategyData.setStrategyClassDatas(strategyClassDatas);
		strategyData.setCurrency(strategyPO.getId().getCurrency());
		return strategyData;
	}

	@Override
	public StrategyPO convertFrom(StrategyData strategyData) {
		StrategyPO strategyPO = new StrategyPO();
		StrategyIdPO strategyIdPO = new StrategyIdPO();
		strategyIdPO.setAccoOwner(strategyData.getAccountData().getOwner());
		strategyPO.setId(strategyIdPO);
		strategyPO.setClassPOs(createClassPOs(strategyData));
		return strategyPO;
	}

	private List<ClassPO> createClassPOs(StrategyData strategyData) {
		List<ClassData> classDatas = strategyData.getStrategyClassDatas();
		List<ClassPO> classPOs = new ArrayList<>();

		if (classDatas != null && !classDatas.isEmpty()) {
			for (ClassData classData : classDatas) {
				ClassPO classPO = new ClassPO();
				ClassIdPO classIdPO = new ClassIdPO();
				classIdPO.setStraAccoOwner(strategyData.getAccountData().getOwner());
				classIdPO.setStraCurrency(strategyData.getCurrency());
				classIdPO.setName(classData.getClassName());
				classPO.setId(classIdPO);
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
				classData.setClassName(classPO.getId().getName());
				classDatas.add(classData);
			}
		}
		return classDatas;
	}

}
