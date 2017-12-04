package org.marceloleite.mercado.business.filter;

import java.util.List;
import java.util.stream.Collectors;

import org.marceloleite.mercado.model.Trade;

public class TradeTypeFilter implements Filter<List<Trade>> {

	private String type;

	public TradeTypeFilter(String type) {
		super();
		this.type = type;
	}

	@Override
	public List<Trade> filter(List<Trade> trades) {
		return trades.stream().filter(t -> type.equals(t.getType())).collect(Collectors.toList());
	}

		
	
}
