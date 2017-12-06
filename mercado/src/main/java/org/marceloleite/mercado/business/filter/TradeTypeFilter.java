package org.marceloleite.mercado.business.filter;

import java.util.List;
import java.util.stream.Collectors;

import org.marceloleite.mercado.model.json.JsonTrade;

public class TradeTypeFilter implements Filter<List<JsonTrade>> {

	private String type;

	public TradeTypeFilter(String type) {
		super();
		this.type = type;
	}

	@Override
	public List<JsonTrade> filter(List<JsonTrade> trades) {
		return trades.stream().filter(t -> type.equals(t.getType())).collect(Collectors.toList());
	}

		
	
}
