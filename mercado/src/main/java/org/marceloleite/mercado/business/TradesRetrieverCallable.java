package org.marceloleite.mercado.business;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.marceloleite.mercado.business.checker.MaxTradesReachedCheck;
import org.marceloleite.mercado.consumer.TradesConsumer;
import org.marceloleite.mercado.model.Cryptocoin;
import org.marceloleite.mercado.model.json.JsonTrade;
import org.marceloleite.mercado.util.formatter.LocalDateTimeToString;
import org.marceloleite.mercado.util.formatter.OldMapJsonTradeFormatter;

public class TradesRetrieverCallable implements Callable<Map<Integer, JsonTrade>> {

	private LocalDateTime from;

	private LocalDateTime to;

	public TradesRetrieverCallable(LocalDateTime from, LocalDateTime to) {
		super();

		this.from = from;
		this.to = to;
	}

	@Override
	public Map<Integer, JsonTrade> call() throws Exception {
		LocalDateTimeToString localDateTimeToString = new LocalDateTimeToString();
		/*System.out.println("Retrieving from " + localDateTimeToString.format(from) + " to "
				+ localDateTimeToString.format(to) + ".");*/
		List<JsonTrade> jsonTrades = new TradesConsumer(Cryptocoin.BITCOIN).consume(from, to);
		Map<Integer, JsonTrade> result;
		if (new MaxTradesReachedCheck().check(jsonTrades)) {

			System.err.println("Warning: Maximum trades exceeded from " + localDateTimeToString.format(from) + " to "
					+ localDateTimeToString.format(to) + ". Splitting execution.");
			result = splitExecution();
		} else {
			result = new OldMapJsonTradeFormatter().format(jsonTrades);
		}
		return result;
	}

	public Map<Integer, JsonTrade> splitExecution() {
		Duration totalDuration = Duration.between(from, to);
		Duration stepDuration = totalDuration.dividedBy(2);
		TradesRetriever tradesRetriever = new TradesRetriever(stepDuration);
		return tradesRetriever.retrieve(from, totalDuration);
	}

}
