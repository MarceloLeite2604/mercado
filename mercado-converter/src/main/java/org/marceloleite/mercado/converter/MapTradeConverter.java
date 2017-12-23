package org.marceloleite.mercado.converter;

import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databasemodel.Trade;
import org.marceloleite.mercado.siteretriever.model.JsonTrade;

public class MapTradeConverter implements Converter<Map<Long, JsonTrade>, Map<Long, Trade>> {

	@Override
	public Map<Long, Trade> convert(Map<Long, JsonTrade> jsonTrades) {
		TradeConverter tradeFormatter = new TradeConverter();
		return jsonTrades.entrySet().stream().map(entry -> tradeFormatter.convert(entry.getValue()))
				.collect(Collectors.toConcurrentMap(Trade::getId, trade -> trade, (oldTrade, newTrade) -> newTrade));
	}
}
