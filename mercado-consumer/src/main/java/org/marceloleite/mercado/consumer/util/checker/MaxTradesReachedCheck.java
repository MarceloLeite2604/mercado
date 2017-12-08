package org.marceloleite.mercado.consumer.util.checker;

import java.util.List;

import org.marceloleite.mercado.commons.interfaces.Check;
import org.marceloleite.mercado.consumer.model.JsonTrade;

public class MaxTradesReachedCheck implements Check<List<JsonTrade>>{
	
	private static int MAXIMUM_TRADES = 1000;

	@Override
	public boolean check(List<JsonTrade> jsonTrades) {
		return (jsonTrades.size() >= MAXIMUM_TRADES);
	}

}
