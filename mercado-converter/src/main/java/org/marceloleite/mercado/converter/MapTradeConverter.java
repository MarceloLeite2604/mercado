package org.marceloleite.mercado.converter;

import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.siteretriever.model.JsonTrade;

public class MapTradeConverter implements Converter<Map<Long, JsonTrade>, Map<Long, TradePO>> {

	@Override
	public Map<Long, TradePO> convert(Map<Long, JsonTrade> jsonTrades) {
		TradeConverter tradeFormatter = new TradeConverter();
		return jsonTrades.entrySet().stream().map(entry -> tradeFormatter.convert(entry.getValue()))
				.collect(Collectors.toConcurrentMap(TradePO::getId, trade -> trade, (oldTrade, newTrade) -> newTrade));
	}
}
