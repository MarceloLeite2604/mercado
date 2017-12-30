package org.marceloleite.mercado.siteretriever.trades;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.siteretriever.model.JsonTrade;
import org.marceloleite.mercado.siteretriever.util.checker.MaxTradesReachedCheck;
import org.marceloleite.mercado.siteretriever.util.converter.ListToMapJsonTradeConverter;

class PartialTradesSiteRetrieverCallable implements Callable<Map<Long, JsonTrade>> {

	private Currency currency;

	private LocalDateTime from;

	private LocalDateTime to;

	public PartialTradesSiteRetrieverCallable(Currency currency, LocalDateTime from, LocalDateTime to) {
		super();

		this.currency = currency;
		this.from = from;
		this.to = to;
	}

	@Override
	public Map<Long, JsonTrade> call() throws Exception {
		LocalDateTimeToStringConverter localDateTimeToString = new LocalDateTimeToStringConverter();
		List<JsonTrade> jsonTrades = new PartialTradesSiteRetriever(currency).retrieve(from, to);
		Map<Long, JsonTrade> result;
		if (new MaxTradesReachedCheck().check(jsonTrades)) {

			System.err.println("Warning: Maximum trades exceeded from " + localDateTimeToString.convert(from) + " to "
					+ localDateTimeToString.convert(to) + ". Splitting execution.");
			result = splitExecution();
		} else {
			result = new ListToMapJsonTradeConverter().convert(jsonTrades);
		}
		return result;
	}

	public Map<Long, JsonTrade> splitExecution() {
		Duration totalDuration = Duration.between(from, to);
		Duration stepDuration = totalDuration.dividedBy(2);
		TradesSiteRetriever tradesRetriever = new TradesSiteRetriever(currency, stepDuration);
		return tradesRetriever.retrieve(from, totalDuration);
	}

}
