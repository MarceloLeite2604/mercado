package org.marceloleite.mercado.business.filter;

import java.util.Map;
import java.util.stream.Collectors;

import org.marceloleite.mercado.model.persistence.Trade;
import org.marceloleite.mercado.model.persistence.TradeType;

public class TradeTypeFilter implements Filter<Map<Integer, Trade>> {

	private TradeType type;

	public TradeTypeFilter(TradeType type) {
		super();
		this.type = type;
	}

	@Override
	public Map<Integer, Trade> filter(Map<Integer, Trade> trades) {
		return trades.entrySet()
			.stream()
			.filter(entry -> type.equals(entry.getValue()
				.getTradeType()))
			.map(entry -> entry.getValue())
			.collect(Collectors.toConcurrentMap(Trade::getId, trade -> trade, (oldTrade, newTrade) -> newTrade));
	}

}
