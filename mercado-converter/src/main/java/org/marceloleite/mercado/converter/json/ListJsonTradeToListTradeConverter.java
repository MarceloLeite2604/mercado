package org.marceloleite.mercado.converter.json;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTrade;

public class ListJsonTradeToListTradeConverter implements Converter<List<JsonTrade>, List<TradePO>> {

	@Override
	public List<TradePO> convertTo(List<JsonTrade> jsonTrades) {
		TradeConverter tradeConverter = new TradeConverter();
		List<TradePO> trades = new ArrayList<>();
		for ( JsonTrade jsonTrade : jsonTrades) {
			trades.add(tradeConverter.convertTo(jsonTrade));
		}
		return trades;
	}

	@Override
	public List<JsonTrade> convertFrom(List<TradePO> object) {
		throw new UnsupportedOperationException();
	}

}
