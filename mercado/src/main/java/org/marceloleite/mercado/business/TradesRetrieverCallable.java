package org.marceloleite.mercado.business;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

import org.marceloleite.mercado.business.checker.MaxTradesReachedCheck;
import org.marceloleite.mercado.consumer.TradesConsumer;
import org.marceloleite.mercado.model.Cryptocoin;
import org.marceloleite.mercado.model.json.JsonTrade;

public class TradesRetrieverCallable implements Callable<List<JsonTrade>> {

	private Calendar from;

	private Calendar to;

	public TradesRetrieverCallable(Calendar from, Calendar to) {
		super();

		this.from = from;
		this.to = to;
	}

	@Override
	public List<JsonTrade> call() throws Exception {
		List<JsonTrade> jsonTrades = new TradesConsumer(Cryptocoin.BITCOIN).consume(from, to);
		if (new MaxTradesReachedCheck().check(jsonTrades)) {
			System.err.println("Warning: Maximum trades exceeded.");
		}
		return jsonTrades;
	}

}
