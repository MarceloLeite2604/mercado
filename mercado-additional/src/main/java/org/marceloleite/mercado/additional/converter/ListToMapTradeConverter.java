package org.marceloleite.mercado.additional.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databasemodel.TradePO;

public class ListToMapTradeConverter implements Converter<List<TradePO>, Map<Long, TradePO>> {

	@Override
	public Map<Long, TradePO> convert(List<TradePO> trades) {
		return trades.stream().map(trade -> trade)
				.collect(Collectors.toConcurrentMap(tradePO -> tradePO.getTradeIdPO().getId(), trade -> trade, (oldTrade, newTrade) -> newTrade));
	}

}
