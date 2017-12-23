package org.marceloleite.mercado.additional.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databasemodel.Trade;

public class ListToMapTradeConverter implements Converter<List<Trade>, Map<Long, Trade>> {

	@Override
	public Map<Long, Trade> convert(List<Trade> trades) {
		return trades.stream().map(trade -> trade)
				.collect(Collectors.toConcurrentMap(Trade::getId, trade -> trade, (oldTrade, newTrade) -> newTrade));
	}

}
