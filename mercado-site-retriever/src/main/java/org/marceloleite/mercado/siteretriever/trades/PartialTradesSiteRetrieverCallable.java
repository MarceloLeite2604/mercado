package org.marceloleite.mercado.siteretriever.trades;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.converter.TimeIntervalToStringConverter;
import org.marceloleite.mercado.jsonmodel.JsonTrade;
import org.marceloleite.mercado.siteretriever.util.checker.MaxTradesReachedCheck;
import org.marceloleite.mercado.siteretriever.util.converter.ListToMapJsonTradeConverter;

class PartialTradesSiteRetrieverCallable implements Callable<Map<Long, JsonTrade>> {

	private Currency currency;

	private TimeInterval timeInterval;

	public PartialTradesSiteRetrieverCallable(Currency currency, TimeInterval timeInterval) {
		super();

		this.currency = currency;
		this.timeInterval = timeInterval;
	}

	@Override
	public Map<Long, JsonTrade> call() throws Exception {
		List<JsonTrade> jsonTrades = new PartialTradesSiteRetriever(currency).retrieve(timeInterval);
		Map<Long, JsonTrade> result;
		if (new MaxTradesReachedCheck().check(jsonTrades)) {
			TimeIntervalToStringConverter timeIntervalToStringConverter = new TimeIntervalToStringConverter();
			System.err.println("Warning: Maximum trades exceeded from " + timeIntervalToStringConverter.convertTo(timeInterval) + ". Splitting execution.");
			result = splitExecution();
		} else {
			result = new ListToMapJsonTradeConverter().convertTo(jsonTrades);
		}
		return result;
	}

	public Map<Long, JsonTrade> splitExecution() {
		TradesSiteRetriever tradesRetriever = new TradesSiteRetriever(currency, timeInterval.getDuration().dividedBy(2));
		return tradesRetriever.retrieve(timeInterval);
	}

}
