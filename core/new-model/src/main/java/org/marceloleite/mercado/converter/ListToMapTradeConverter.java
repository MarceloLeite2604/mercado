package org.marceloleite.mercado.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.model.Trade;

public final class ListToMapTradeConverter {
	
	private ListToMapTradeConverter() {
	}

	public static Map<Long, Trade> fromListToMap(List<Trade> trades) {
		return trades.stream()
			.map(jsonTrade -> jsonTrade)
			.collect(Collectors.toConcurrentMap(Trade::getId, trade -> trade, (oldModel, newModel) -> newModel));
	}

	public static List<Trade> fromMapToList(Map<Long, Trade> tradeMap) {
		return tradeMap.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
	}
	
}
