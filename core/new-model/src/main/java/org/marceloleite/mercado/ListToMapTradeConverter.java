package org.marceloleite.mercado;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.model.Trade;

public class ListToMapTradeConverter implements Converter<List<Trade>, Map<Long, Trade>> {
	
	private static ListToMapTradeConverter instance;
	
	private ListToMapTradeConverter() {
	}

	@Override
	public Map<Long, Trade> convertTo(List<Trade> trades) {
		return trades.stream()
			.map(jsonTrade -> jsonTrade)
			.collect(Collectors.toConcurrentMap(Trade::getId, trade -> trade, (oldModel, newModel) -> newModel));
	}

	@Override
	public List<Trade> convertFrom(Map<Long, Trade> tradeMap) {
		return tradeMap.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
	}
	
	public static ListToMapTradeConverter getInstance() {
		if (instance == null) {
			instance = new ListToMapTradeConverter();
		}
		return instance;
	}

}
