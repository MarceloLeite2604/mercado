package org.marceloleite.mercado;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.TradeType;
import org.marceloleite.mercado.commons.interfaces.Filter;
import org.marceloleite.mercado.model.Trade;

public class TradeTypeFilter implements Filter<Map<Long, Trade>> {

	private TradeType type;

	public TradeTypeFilter(TradeType type) {
		super();
		this.type = type;
	}

	@Override
	public Map<Long, Trade> filter(Map<Long, Trade> trades) {
		return trades.entrySet().stream().filter(entry -> type.equals(entry.getValue().getType()))
				.map(Entry<Long, Trade>::getValue).collect(Collectors.toConcurrentMap(
						trade -> trade.getId(), trade -> trade, (oldTrade, newTrade) -> newTrade));
	}

}
