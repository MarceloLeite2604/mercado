package org.marceloleite.mercado.modeler.util.formatter;

import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.interfaces.Formatter;
import org.marceloleite.mercado.consumer.model.JsonTrade;
import org.marceloleite.mercado.modeler.persistence.Trade;

public class MapTradeFormatter implements Formatter<Map<Integer, JsonTrade>, Map<Integer, Trade>> {

	@Override
	public Map<Integer, Trade> format(Map<Integer, JsonTrade> jsonTrades) {
		TradeFormatter tradeFormatter = new TradeFormatter();
		return jsonTrades.entrySet()
			.stream()
			.map(entry -> tradeFormatter.format(entry.getValue()))
			.collect(Collectors.toConcurrentMap(Trade::getId, trade -> trade,
					(oldTrade, newTrade) -> newTrade));
	}
}
