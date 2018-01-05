package org.marceloleite.mercado.siteretriever.trades;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.interfaces.Retriever;
import org.marceloleite.mercado.jsonmodel.JsonTrade;
import org.marceloleite.mercado.siteretriever.AbstractSiteRetriever;

public class TradesSiteRetriever extends AbstractSiteRetriever implements Retriever<Map<Long, JsonTrade>> {

	private static final Duration DEFAULT_DURATION_STEP = Duration.ofMinutes(30);
	
	private static final int THREAD_POOL_SIZE = 8;

	private Duration stepDuration;

	public TradesSiteRetriever(Currency currency, Duration stepDuration) {
		super(currency);
		this.stepDuration = stepDuration;
	}

	public TradesSiteRetriever(Currency currency) {
		this(currency, DEFAULT_DURATION_STEP);
	}

	public void setStepDuration(Duration stepDuration) {
		this.stepDuration = stepDuration;
	}

	/*
	 * public Map<Long, JsonTrade> retrieve(Currency currency, LocalDateTime
	 * initialTime, Duration duration) {
	 */
	public Map<Long, JsonTrade> retrieve(Object... args) {
		Set<Future<Map<Long, JsonTrade>>> futureSet = new HashSet<>();
		
		if ( !(args[0] instanceof LocalDateTime)) {
			throw new IllegalArgumentException("First parameter must be of type \""+LocalDateTime.class+"\".");
		}
		
		if ( !(args[1] instanceof Duration)) {
			throw new IllegalArgumentException("Second parameter must be of type \""+Duration.class+"\".");
		}
		
		LocalDateTime initialTime = (LocalDateTime)args[0];
		Duration duration = (Duration)args[1];
		
		long totalSteps = calculateTotalSteps(duration);
		ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

		LocalDateTime from = initialTime;
		Duration nextStepDuration = calculateStepDuration(from, initialTime, duration);
		LocalDateTime to = LocalDateTime.from(initialTime).plus(nextStepDuration);

		for (int step = 0; step < totalSteps; step++) {
			Callable<Map<Long, JsonTrade>> partialTradesSiteRetrieverCallable = new PartialTradesSiteRetrieverCallable(currency, from,
					to);
			futureSet.add(executorService.submit(partialTradesSiteRetrieverCallable));
			from = LocalDateTime.from(from).plus(nextStepDuration);
			nextStepDuration = calculateStepDuration(from, initialTime, duration);
			to = LocalDateTime.from(to).plus(nextStepDuration);
		}

		Map<Long, JsonTrade> trades = new ConcurrentHashMap<>();
		for (Future<Map<Long, JsonTrade>> future : futureSet) {
			try {
				Map<Long, JsonTrade> jsonTrades = future.get();
				trades.putAll(jsonTrades);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		executorService.shutdownNow();

		return trades;
	}

	private long calculateTotalSteps(Duration duration) {
		long totalSteps = (duration.getSeconds() / stepDuration.getSeconds());
		totalSteps = (totalSteps == 0 ? 1 : totalSteps);
		return totalSteps;
	}

	public Duration calculateStepDuration(LocalDateTime stepStartTime, LocalDateTime initialTime, Duration duration) {
		Duration remainingDuration = Duration.between(stepStartTime, initialTime.plus(duration));
		if (stepDuration.compareTo(remainingDuration) > 0) {
			return remainingDuration;
		} else {
			return stepDuration;
		}
	}

	@Override
	protected String getMethod() {
		return null;
	}
}
