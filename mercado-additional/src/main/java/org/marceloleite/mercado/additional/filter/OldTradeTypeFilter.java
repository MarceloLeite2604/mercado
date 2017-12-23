package org.marceloleite.mercado.additional.filter;

import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.interfaces.Filter;
import org.marceloleite.mercado.databasemodel.Trade;
import org.marceloleite.mercado.databasemodel.TradeType;

public class OldTradeTypeFilter implements Filter<Map<Long, Trade>> {

	private TradeType type;

	public OldTradeTypeFilter(TradeType type) {
		super();
		this.type = type;
	}

	@Override
	public Map<Long, Trade> filter(Map<Long, Trade> trades) {
		return trades.entrySet().stream().filter(entry -> type.equals(entry.getValue().getTradeType()))
				.map(entry -> entry.getValue())
				.collect(Collectors.toConcurrentMap(Trade::getId, trade -> trade, (oldTrade, newTrade) -> newTrade));
	}

}
