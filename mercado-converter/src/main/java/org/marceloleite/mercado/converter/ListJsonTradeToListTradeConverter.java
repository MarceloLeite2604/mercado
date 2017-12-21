package org.marceloleite.mercado.converter;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databasemodel.Trade;
import org.marceloleite.mercado.siteretriever.model.JsonTrade;

public class ListJsonTradeToListTradeConverter implements Converter<List<JsonTrade>, List<Trade>> {

	@Override
	public List<Trade> convert(List<JsonTrade> jsonTrades) {
		TradeConverter tradeConverter = new TradeConverter();
		List<Trade> trades = new ArrayList<>();
		for ( JsonTrade jsonTrade : jsonTrades) {
			trades.add(tradeConverter.convert(jsonTrade));
		}
		return trades;
	}

}
