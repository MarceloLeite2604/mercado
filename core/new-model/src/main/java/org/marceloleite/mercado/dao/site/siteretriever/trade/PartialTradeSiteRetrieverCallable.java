package org.marceloleite.mercado.dao.site.siteretriever.trade;

import java.util.List;
import java.util.concurrent.Callable;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.dao.site.siteretriever.trade.checker.MaxTradesReachedChecker;
import org.marceloleite.mercado.model.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PartialTradeSiteRetrieverCallable implements Callable<List<Trade>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TradeSiteRetriever.class);
	
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
			LOGGER.warn("Maximum trades exceeded from {}. Splitting execution.", timeInterval);
			trades = splitExecution();
		} 
		return trades;
	}

	public List<Trade> splitExecution() {
		TradeSiteRetriever tradesRetriever = new TradeSiteRetriever(timeInterval.getDuration().dividedBy(2));
		return tradesRetriever.retrieve(currency, timeInterval);
	}
}
