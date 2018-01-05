package org.marceloleite.mercado.siteretriever.util.checker;

import java.util.List;

import org.marceloleite.mercado.commons.interfaces.Check;
import org.marceloleite.mercado.jsonmodel.JsonTrade;

public class MaxTradesReachedCheck implements Check<List<JsonTrade>>{
	
	private static int MAXIMUM_TRADES = 1000;

	@Override
	public boolean check(List<JsonTrade> jsonTrades) {
		return (jsonTrades.size() >= MAXIMUM_TRADES);
	}

}
