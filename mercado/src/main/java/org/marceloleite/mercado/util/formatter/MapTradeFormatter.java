package org.marceloleite.mercado.util.formatter;

import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.model.json.JsonTrade;
import org.marceloleite.mercado.model.persistence.Trade;

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
