package org.marceloleite.mercado.converter.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTrade;

public class MapJsonTradeToListTradeConverter implements Converter<Map<Long, JsonTrade>, List<TradePO>> {

	@Override
	public List<TradePO> convertTo(Map<Long, JsonTrade> jsonTrades) {
		TradeConverter tradeConverter = new TradeConverter();
		List<TradePO> trades = new ArrayList<>();
		for ( Long tid : jsonTrades.keySet()) {
			JsonTrade jsonTrade = jsonTrades.get(tid);
			trades.add(tradeConverter.convertTo(jsonTrade));
		}
		return trades;
	}

	@Override
	public Map<Long, JsonTrade> convertFrom(List<TradePO> object) {
		throw new UnsupportedOperationException();
	}

}
