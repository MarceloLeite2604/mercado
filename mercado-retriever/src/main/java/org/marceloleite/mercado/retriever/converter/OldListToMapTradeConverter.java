package org.marceloleite.mercado.retriever.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TradePO;

public class OldListToMapTradeConverter implements Converter<List<TradePO>, Map<Long, TradePO>> {

	@Override
	public Map<Long, TradePO> convertTo(List<TradePO> trades) {
		return trades.stream().map(trade -> trade)
				.collect(Collectors.toConcurrentMap(tradePO -> tradePO.getTradeIdPO().getId(), trade -> trade, (oldTrade, newTrade) -> newTrade));
	}

	@Override
	public List<TradePO> convertFrom(Map<Long, TradePO> object) {
		throw new UnsupportedOperationException();
	}

}
