package org.marceloleite.mercado.siteretriever.trades;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.model.Trade;
import org.marceloleite.mercado.siteretriever.converter.ListToMapTradeConverter;
import org.marceloleite.mercado.siteretriever.trades.checker.MaxTradesReachedChecker;

class PartialTradesSiteRetrieverCallable implements Callable<Map<Long, Trade>> {
	
	private static final MaxTradesReachedChecker MAX_TRADES_REACHED_CHECKER = new MaxTradesReachedChecker();
	
	private static final ListToMapTradeConverter LIST_TO_MAP_TRADE_CONVERTER = new ListToMapTradeConverter();

	private Currency currency;

	private TimeInterval timeInterval;

	public PartialTradesSiteRetrieverCallable(Currency currency, TimeInterval timeInterval) {
		super();

		this.currency = currency;
		this.timeInterval = timeInterval;
	}

	@Override
	public Map<Long, Trade> call() throws Exception {
		List<Trade> trades = new PartialTradesSiteRetriever().retrieve(currency, timeInterval);
		Map<Long, Trade> result;
		
		if (MAX_TRADES_REACHED_CHECKER.check(trades)) {
			System.err.println("Warning: Maximum trades exceeded from " + timeInterval + ". Splitting execution.");
			result = splitExecution();
		} else {
			result = LIST_TO_MAP_TRADE_CONVERTER.convertTo(trades);
		}
		return result;
	}

	public Map<Long, Trade> splitExecution() {
		TradesSiteRetriever tradesRetriever = new TradesSiteRetriever(timeInterval.getDuration().dividedBy(2));
		return tradesRetriever.retrieve(currency, timeInterval);
	}
}
