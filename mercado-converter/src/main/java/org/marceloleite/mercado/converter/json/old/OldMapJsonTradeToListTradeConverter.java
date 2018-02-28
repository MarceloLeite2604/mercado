package org.marceloleite.mercado.converter.json.old;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TradePO;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTrade;

public class OldMapJsonTradeToListTradeConverter implements Converter<Map<Long, JsonTrade>, List<TradePO>> {

	@Override
	public List<TradePO> convertTo(Map<Long, JsonTrade> jsonTrades) {
		OldTradeConverter tradeConverter = new OldTradeConverter();
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
