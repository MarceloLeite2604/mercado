package org.marceloleite.mercado.converter.json.api.data;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.data.Trade;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTrade;

public class ListJsonTradeToListTradeConverter implements Converter<List<JsonTrade>, List<Trade>> {

	@Override
	public List<Trade> convertTo(List<JsonTrade> jsonTrades) {
		JsonTradeToTradeConverter jsonTradeToTradeConverter = new JsonTradeToTradeConverter();
		List<Trade> trades = new ArrayList<>();
		for (JsonTrade jsonTrade : jsonTrades) {
			trades.add(jsonTradeToTradeConverter.convertTo(jsonTrade));
		}
		return trades;
	}

	@Override
	public List<JsonTrade> convertFrom(List<Trade> trades) {
		throw new UnsupportedOperationException();
	}

}
