package org.marceloleite.mercado.siteretriever.trades;

import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.Produces;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.model.Trade;
import org.marceloleite.mercado.siteretriever.AbstractSiteRetriever;
import org.springframework.stereotype.Component;

@Component
public class TradesSiteRetriever extends AbstractSiteRetriever {

	private static final Duration DEFAULT_DURATION_STEP = Duration.ofMinutes(30);

	private static final int DEFAULT_THREAD_POOL_SIZE = 4;

	private static Duration configuredStepDuration = DEFAULT_DURATION_STEP;

	// private static int configuredThreadPoolSize = DEFAULT_THREAD_POOL_SIZE;

	private static ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);

	private Duration stepDuration;

	// private int threadPoolSize;

	public TradesSiteRetriever(Duration stepDuration) {
		super();
		this.stepDuration = stepDuration;
		// this.threadPoolSize = configuredThreadPoolSize;
	}

	public TradesSiteRetriever() {
		this(configuredStepDuration);
	}

	public Map<Long, Trade> retrieve(Currency currency, TimeInterval timeInterval) {
		Set<Future<Map<Long, Trade>>> futureSet = new HashSet<>();

		checkArguments(timeInterval);

		TimeDivisionController timeDivisionController = new TimeDivisionController(timeInterval, stepDuration);
		for (TimeInterval timeIntervalDivision : timeDivisionController.geTimeIntervals()) {
			Callable<Map<Long, Trade>> partialTradesSiteRetrieverCallable = createPartialTradesSiteRetrieverCallable(
					currency, timeIntervalDivision);
			futureSet.add(executorService.submit(partialTradesSiteRetrieverCallable));
		}

		Map<Long, Trade> trades = new ConcurrentHashMap<>();
		for (Future<Map<Long, Trade>> future : futureSet) {
			try {
				Map<Long, Trade> tradesRetrieved = future.get();
				trades.putAll(tradesRetrieved);
			} catch (InterruptedException | ExecutionException exception) {
				exception.printStackTrace();
			}
		}

		// executorService.shutdownNow();

		return trades;
	}

	private void checkArguments(TimeInterval timeInterval) {
		if (timeInterval == null) {
			throw new IllegalArgumentException("Time interval cannot be null.");
		}
	}

	@Produces
	private PartialTradesSiteRetrieverCallable createPartialTradesSiteRetrieverCallable(Currency currency,
			TimeInterval timeInterval) {
		return new PartialTradesSiteRetrieverCallable(currency, timeInterval);
	}

	@Override
	protected String getMethod() {
		throw new UnsupportedOperationException();
	}

	public static void setConfiguredStepDuration(Duration configuredStepDuration) {
		TradesSiteRetriever.configuredStepDuration = configuredStepDuration;
	}

	public static void setConfiguredThreadPoolSize(int configuredThreadPoolSize) {
		executorService = Executors.newFixedThreadPool(configuredThreadPoolSize);
	}
}
