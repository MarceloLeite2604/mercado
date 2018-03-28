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

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.data.Trade;
import org.marceloleite.mercado.siteretriever.AbstractSiteRetriever;

public class TradesSiteRetriever extends AbstractSiteRetriever {

	private static final Duration DEFAULT_DURATION_STEP = Duration.ofMinutes(30);
<<<<<<< HEAD

	private static final int DEFAULT_THREAD_POOL_SIZE = 2;

	private static Duration configuredStepDuration = DEFAULT_DURATION_STEP;

	private static int configuredThreadPoolSize = DEFAULT_THREAD_POOL_SIZE;

	private Duration stepDuration;

	private int threadPoolSize;

	public TradesSiteRetriever(Currency currency, Duration stepDuration) {
		super(currency);
		this.stepDuration = stepDuration;
		this.threadPoolSize = configuredThreadPoolSize;
	}
	
	public TradesSiteRetriever(Currency currency) {
		this(currency, configuredStepDuration);
	}

	public Map<Long, Trade> retrieve(TimeInterval timeInterval) {
		Set<Future<Map<Long, Trade>>> futureSet = new HashSet<>();

		checkArguments(timeInterval);

		ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);

		TimeDivisionController timeDivisionController = new TimeDivisionController(timeInterval, stepDuration);
		for (TimeInterval timeIntervalDivision : timeDivisionController.geTimeIntervals()) {
			Callable<Map<Long, Trade>> partialTradesSiteRetrieverCallable = new PartialTradesSiteRetrieverCallable(
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

		executorService.shutdownNow();

		return trades;
	}

	private void checkArguments(TimeInterval timeInterval) {
		if (timeInterval == null) {
			throw new IllegalArgumentException("Time interval cannot be null.");
		}
	}

	@Override
	protected String getMethod() {
		throw new UnsupportedOperationException();
	}

	public static void setConfiguredStepDuration(Duration configuredStepDuration) {
		TradesSiteRetriever.configuredStepDuration = configuredStepDuration;
	}

	public static void setConfiguredThreadPoolSize(int configuredThreadPoolSize) {
		TradesSiteRetriever.configuredThreadPoolSize = configuredThreadPoolSize;
=======
	
	private static final int THREAD_POOL_SIZE = 2;

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

	public Map<Long, Trade> retrieve(TimeInterval timeInterval) {
		Set<Future<Map<Long, Trade>>> futureSet = new HashSet<>();
		
		checkArguments(timeInterval);
		
		ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

		TimeDivisionController timeDivisionController = new TimeDivisionController(timeInterval, stepDuration);
		for (TimeInterval timeIntervalDivision : timeDivisionController.geTimeIntervals()) {
			Callable<Map<Long, Trade>> partialTradesSiteRetrieverCallable = new PartialTradesSiteRetrieverCallable(currency, timeIntervalDivision);
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

		executorService.shutdownNow();

		return trades;
	}

	private void checkArguments(TimeInterval timeInterval) {
		if ( timeInterval == null ) {
			throw new IllegalArgumentException("Time interval cannot be null.");
		}
	}

	@Override
	protected String getMethod() {
		throw new UnsupportedOperationException();
>>>>>>> branch 'develop_1' of git@github.com:MarceloLeite2604/mercado.git
	}
}
