package org.marceloleite.mercado.converter.datamodel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.database.data.structure.TradeDataModel;

public class ListToMapTradeDataModelConverter implements Converter<List<TradeDataModel>, Map<Long, TradeDataModel>> {

	@Override
	public Map<Long, TradeDataModel> convertTo(List<TradeDataModel> tradeDataModelList) {
		return tradeDataModelList.stream()
			.map(jsonTrade -> jsonTrade)
			.collect(Collectors.toConcurrentMap(TradeDataModel::getId, tradeDataModel -> tradeDataModel, (oldTradeDataModel, newTradeDataModel) -> newTradeDataModel));
	}

	@Override
	public List<TradeDataModel> convertFrom(Map<Long, TradeDataModel> tradeDataModelMap) {
		return tradeDataModelMap.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
	}

}
