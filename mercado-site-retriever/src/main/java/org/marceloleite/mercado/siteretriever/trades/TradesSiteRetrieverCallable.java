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

public class TradesSiteRetrieverCallable implements Callable<Map<Integer, JsonTrade>> {

	private Currency currency;

	private LocalDateTime from;

	private LocalDateTime to;

	public TradesSiteRetrieverCallable(Currency currency, LocalDateTime from, LocalDateTime to) {
		super();

		this.currency = currency;
		this.from = from;
		this.to = to;
	}

	@Override
	public Map<Integer, JsonTrade> call() throws Exception {
		LocalDateTimeToStringConverter localDateTimeToString = new LocalDateTimeToStringConverter();
		List<JsonTrade> jsonTrades = new TradesSiteRetriever(currency).retrieve(from, to);
		Map<Integer, JsonTrade> result;
		if (new MaxTradesReachedCheck().check(jsonTrades)) {

			System.err.println("Warning: Maximum trades exceeded from " + localDateTimeToString.convert(from) + " to "
					+ localDateTimeToString.convert(to) + ". Splitting execution.");
			result = splitExecution();
		} else {
			result = new ListToMapJsonTradeConverter().convert(jsonTrades);
		}
		return result;
	}

	public Map<Integer, JsonTrade> splitExecution() {
		Duration totalDuration = Duration.between(from, to);
		Duration stepDuration = totalDuration.dividedBy(2);
		TradesSiteRetrieverController tradesRetriever = new TradesSiteRetrieverController(stepDuration);
		return tradesRetriever.retrieve(currency, from, totalDuration);
	}

}
