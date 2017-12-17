package org.marceloleite.mercado.modeler.util.converter;

import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.interfaces.Converter;
import org.marceloleite.mercado.consumer.model.JsonTrade;
import org.marceloleite.mercado.modeler.persistence.model.Trade;

public class MapTradeConverter implements Converter<Map<Integer, JsonTrade>, Map<Integer, Trade>> {

	@Override
	public Map<Integer, Trade> format(Map<Integer, JsonTrade> jsonTrades) {
		TradeConverter tradeFormatter = new TradeConverter();
		return jsonTrades.entrySet()
			.stream()
			.map(entry -> tradeFormatter.format(entry.getValue()))
			.collect(Collectors.toConcurrentMap(Trade::getId, trade -> trade,
					(oldTrade, newTrade) -> newTrade));
	}
}
