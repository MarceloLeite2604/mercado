package org.marceloleite.mercado.converter.json.old;

import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TradePO;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTrade;

public class OldMapTradeConverter implements Converter<Map<Long, JsonTrade>, Map<Long, TradePO>> {

	@Override
	public Map<Long, TradePO> convertTo(Map<Long, JsonTrade> jsonTrades) {
		OldTradeConverter tradeFormatter = new OldTradeConverter();
		return jsonTrades.entrySet().stream().map(entry -> tradeFormatter.convertTo(entry.getValue()))
				.collect(Collectors.toConcurrentMap(tradePO -> tradePO.getTradeIdPO().getId(), trade -> trade, (oldTrade, newTrade) -> newTrade));
	}

	@Override
	public Map<Long, JsonTrade> convertFrom(Map<Long, TradePO> object) {
		throw new UnsupportedOperationException();
	}
}