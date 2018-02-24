package org.marceloleite.mercado.siteretriever.trades;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.converter.datamodel.ListToMapTradeConverter;
import org.marceloleite.mercado.simulator.Trade;
import org.marceloleite.mercado.siteretriever.util.checker.MaxTradesReachedCheck;

class PartialTradesSiteRetrieverCallable implements Callable<Map<Long, Trade>> {

	private Currency currency;

	private TimeInterval timeInterval;

	public PartialTradesSiteRetrieverCallable(Currency currency, TimeInterval timeInterval) {
		super();

		this.currency = currency;
		this.timeInterval = timeInterval;
	}

	@Override
	public Map<Long, Trade> call() throws Exception {
		List<Trade> trades = new PartialTradesSiteRetriever(currency).retrieve(timeInterval);
		Map<Long, Trade> result;
		if (new MaxTradesReachedCheck().check(trades)) {
			System.err.println("Warning: Maximum trades exceeded from " + timeInterval + ". Splitting execution.");
			result = splitExecution();
		} else {
			result = new ListToMapTradeConverter().convertTo(trades);
		}
		return result;
	}

	public Map<Long, Trade> splitExecution() {
		TradesSiteRetriever tradesRetriever = new TradesSiteRetriever(currency, timeInterval.getDuration().dividedBy(2));
		return tradesRetriever.retrieve(timeInterval);
	}

}
