package org.marceloleite.mercado.dao.json.siteretriever.trade.checker;

import java.util.List;

import org.marceloleite.mercado.commons.interfaces.Checker;
import org.marceloleite.mercado.model.Trade;

public class MaxTradesReachedChecker implements Checker<List<Trade>>{
	
	private static int MAXIMUM_TRADES = 1000;
	
	private static MaxTradesReachedChecker instance;

	@Override
	public boolean check(List<Trade> trades) {
		return (trades.size() >= MAXIMUM_TRADES);
	}
	
	public static MaxTradesReachedChecker getInstance() {
		if ( instance == null ) {
			instance = new MaxTradesReachedChecker();
		}
		return instance;
	}

}
