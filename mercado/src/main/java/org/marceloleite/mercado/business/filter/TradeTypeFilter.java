package org.marceloleite.mercado.business.filter;

import java.util.List;
import java.util.stream.Collectors;

import org.marceloleite.mercado.model.persistence.Trade;
import org.marceloleite.mercado.model.persistence.TradeType;

public class TradeTypeFilter implements Filter<List<Trade>> {

	private TradeType type;

	public TradeTypeFilter(TradeType type) {
		super();
		this.type = type;
	}

	@Override
	public List<Trade> filter(List<Trade> trades) {
		return trades.stream().filter(trade -> type.equals(trade.getTradeType())).collect(Collectors.toList());
	}

		
	
}
