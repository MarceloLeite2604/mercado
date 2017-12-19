package org.marceloleite.mercado.consumer.trades;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.consumer.model.JsonTrade;
import org.marceloleite.mercado.consumer.util.checker.MaxTradesReachedCheck;
import org.marceloleite.mercado.consumer.util.formatter.ListToMapJsonTradeFormatter;

public class TradesRetrieverCallable implements Callable<Map<Integer, JsonTrade>> {

	private Currency cryptocoin;

	private LocalDateTime from;

	private LocalDateTime to;

	public TradesRetrieverCallable(Currency cryptocoin, LocalDateTime from, LocalDateTime to) {
		super();

		this.cryptocoin = cryptocoin;
		this.from = from;
		this.to = to;
	}

	@Override
	public Map<Integer, JsonTrade> call() throws Exception {
		LocalDateTimeToStringConverter localDateTimeToString = new LocalDateTimeToStringConverter();
		/*
		 * System.out.println("Retrieving from " + localDateTimeToString.format(from) +
		 * " to " + localDateTimeToString.format(to) + ".");
		 */
		List<JsonTrade> jsonTrades = new TradesConsumer(cryptocoin).consume(from, to);
		Map<Integer, JsonTrade> result;
		if (new MaxTradesReachedCheck().check(jsonTrades)) {

			System.err.println("Warning: Maximum trades exceeded from " + localDateTimeToString.convert(from) + " to "
					+ localDateTimeToString.convert(to) + ". Splitting execution.");
			result = splitExecution();
		} else {
			result = new ListToMapJsonTradeFormatter().convert(jsonTrades);
		}
		return result;
	}

	public Map<Integer, JsonTrade> splitExecution() {
		Duration totalDuration = Duration.between(from, to);
		Duration stepDuration = totalDuration.dividedBy(2);
		TradesRetriever tradesRetriever = new TradesRetriever(stepDuration);
		return tradesRetriever.retrieve(cryptocoin, from, totalDuration);
	}

}
