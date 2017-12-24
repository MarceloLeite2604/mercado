package org.marceloleite.mercado.converter;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.siteretriever.model.JsonTrade;

public class ListJsonTradeToListTradeConverter implements Converter<List<JsonTrade>, List<TradePO>> {

	@Override
	public List<TradePO> convert(List<JsonTrade> jsonTrades) {
		TradeConverter tradeConverter = new TradeConverter();
		List<TradePO> trades = new ArrayList<>();
		for ( JsonTrade jsonTrade : jsonTrades) {
			trades.add(tradeConverter.convert(jsonTrade));
		}
		return trades;
	}

}
