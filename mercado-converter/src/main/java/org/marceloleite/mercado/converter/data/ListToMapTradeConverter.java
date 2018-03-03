package org.marceloleite.mercado.converter.data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.Trade;

public class ListToMapTradeConverter implements Converter<List<Trade>, Map<Long, Trade>> {

	@Override
	public Map<Long, Trade> convertTo(List<Trade> tradeList) {
		return tradeList.stream()
			.map(jsonTrade -> jsonTrade)
			.collect(Collectors.toConcurrentMap(Trade::getId, trade -> trade, (oldModel, newModel) -> newModel));
	}

	@Override
	public List<Trade> convertFrom(Map<Long, Trade> tradeMap) {
		return tradeMap.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
	}

}
