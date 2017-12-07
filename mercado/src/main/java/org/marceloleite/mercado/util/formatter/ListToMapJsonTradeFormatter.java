package org.marceloleite.mercado.util.formatter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.model.json.JsonTrade;
import org.marceloleite.mercado.model.persistence.Trade;

public class ListToMapJsonTradeFormatter implements Formatter<List<JsonTrade>, Map<Integer, Trade>> {

	@Override
	public Map<Integer, Trade> format(List<JsonTrade> jsonTrades) {
		TradeFormatter tradeFormatter = new TradeFormatter();
		return jsonTrades.stream()
			.map(jsonTrade -> tradeFormatter.format(jsonTrade))
			.collect(Collectors.toConcurrentMap(Trade::getId, trade -> trade, (oldTrade, newTrade) -> newTrade));
	}

}
