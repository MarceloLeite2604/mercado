package org.marceloleite.mercado.tickergenerator.filter;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.interfaces.Filter;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databasemodel.TradeType;

public class TradeTypeFilter implements Filter<Map<Long, TradePO>> {

	private TradeType type;

	public TradeTypeFilter(TradeType type) {
		super();
		this.type = type;
	}

	@Override
	public Map<Long, TradePO> filter(Map<Long, TradePO> trades) {
		return trades.entrySet().stream().filter(entry -> type.equals(entry.getValue().getTradeType()))
				.map(Entry<Long, TradePO>::getValue)
				.collect(Collectors.toConcurrentMap(tradePO -> tradePO.getTradeIdPO().getId(), trade -> trade, (oldTrade, newTrade) -> newTrade));
	}

}
