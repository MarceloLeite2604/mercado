package org.marceloleite.mercado.dao.json.siteretriever.trade;

import java.util.List;
import java.util.concurrent.Callable;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.dao.json.siteretriever.trade.checker.MaxTradesReachedChecker;
import org.marceloleite.mercado.model.Trade;

class PartialTradeSiteRetrieverCallable implements Callable<List<Trade>> {

	private Currency currency;

	private TimeInterval timeInterval;

	public PartialTradeSiteRetrieverCallable(Currency currency, TimeInterval timeInterval) {
		super();

		this.currency = currency;
		this.timeInterval = timeInterval;
	}

	@Override
	public List<Trade> call() throws Exception {
		List<Trade> trades = new PartialTradeSiteRetriever().retrieve(currency, timeInterval);
		if (MaxTradesReachedChecker.getInstance().check(trades)) {
			System.err.println("Warning: Maximum trades exceeded from " + timeInterval + ". Splitting execution.");
			trades = splitExecution();
		} 
		return trades;
	}

	public List<Trade> splitExecution() {
		TradeSiteRetriever tradesRetriever = new TradeSiteRetriever(timeInterval.getDuration().dividedBy(2));
		return tradesRetriever.retrieve(currency, timeInterval);
	}
}
