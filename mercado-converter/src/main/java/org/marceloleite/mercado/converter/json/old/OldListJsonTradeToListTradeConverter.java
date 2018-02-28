package org.marceloleite.mercado.converter.json.old;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TradePO;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTrade;

public class OldListJsonTradeToListTradeConverter implements Converter<List<JsonTrade>, List<TradePO>> {

	@Override
	public List<TradePO> convertTo(List<JsonTrade> jsonTrades) {
		OldTradeConverter tradeConverter = new OldTradeConverter();
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
