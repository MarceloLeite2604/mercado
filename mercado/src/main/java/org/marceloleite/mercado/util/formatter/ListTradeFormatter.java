package org.marceloleite.mercado.util.formatter;

import java.util.List;
import java.util.stream.Collectors;

import org.marceloleite.mercado.model.json.JsonTrade;
import org.marceloleite.mercado.model.persistence.Trade;

public class ListTradeFormatter implements Formatter<List<JsonTrade>, List<Trade>> {

	@Override
	public List<Trade> format(List<JsonTrade> jsonTrades) {
		TradeFormatter tradeFormatter = new TradeFormatter();
		return jsonTrades.stream().map(jsonTrade -> tradeFormatter.format(jsonTrade)).collect(Collectors.toList());
	}

}
