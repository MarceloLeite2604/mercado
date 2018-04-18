package org.marceloleite.mercado.siteretriever.trades.checker;

import java.util.List;

import org.marceloleite.mercado.commons.interfaces.Checker;
import org.marceloleite.mercado.model.Trade;

public class MaxTradesReachedChecker implements Checker<List<Trade>>{
	
	private static int MAXIMUM_TRADES = 1000;

	@Override
	public boolean check(List<Trade> trades) {
		return (trades.size() >= MAXIMUM_TRADES);
	}

}
