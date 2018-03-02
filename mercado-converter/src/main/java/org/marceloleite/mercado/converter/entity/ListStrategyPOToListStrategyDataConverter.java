package org.marceloleite.mercado.converter.entity;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.data.StrategyData;
import org.marceloleite.mercado.databaseretriever.persistence.objects.StrategyPO;

public class ListStrategyPOToListStrategyDataConverter implements Converter<List<StrategyPO>, List<StrategyData>> {

	@Override
	public List<StrategyData> convertTo(List<StrategyPO> strategyPOs) {
		List<StrategyData> strategyDatas = new ArrayList<>();
		StrategyPOToStrategyDataConverter strategyPOToStrategyDataConverter = new StrategyPOToStrategyDataConverter();
		for(StrategyPO strategyPO : strategyPOs) {
			strategyDatas.add(strategyPOToStrategyDataConverter.convertTo(strategyPO));
		}
		return strategyDatas;
	}

	@Override
	public List<StrategyPO> convertFrom(List<StrategyData> strategyDatas) {
		List<StrategyPO> strategyPOs = new ArrayList<>();
		StrategyPOToStrategyDataConverter strategyPOToStrategyDataConverter = new StrategyPOToStrategyDataConverter();
		for (StrategyData strategyData : strategyDatas) {
			strategyPOs.add(strategyPOToStrategyDataConverter.convertFrom(strategyData));
		}
		return strategyPOs;
	}

}
