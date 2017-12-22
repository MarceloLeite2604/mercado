package org.marceloleite.mercado.additional.filter;

import java.util.List;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.interfaces.Filter;
import org.marceloleite.mercado.databasemodel.Trade;
import org.marceloleite.mercado.databasemodel.TradeType;

public class TradeTypeFilter implements Filter<List<Trade>> {

	private TradeType type;

	public TradeTypeFilter(TradeType type) {
		super();
		this.type = type;
	}

	@Override
	public List<Trade> filter(List<Trade> trades) {
		return trades
			.stream()
			.filter(trade -> type.equals(trade.getTradeType()))
			.collect(Collectors.toList());
	}

}
