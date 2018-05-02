package org.marceloleite.mercado.siteretriever.trades;

import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Produces;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.model.Trade;
import org.marceloleite.mercado.siteretriever.AbstractSiteRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TradesSiteRetriever extends AbstractSiteRetriever {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(TradesSiteRetriever.class);

	private static final Duration DEFAULT_DURATION_STEP = Duration.ofMinutes(30);

	private static final int DEFAULT_THREAD_POOL_SIZE = 4;

	private static Duration configuredStepDuration = DEFAULT_DURATION_STEP;

	private static int configuredThreadPoolSize = DEFAULT_THREAD_POOL_SIZE;

	private static ThreadPoolExecutor executorService;

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
				Map<Long, Trade> tradesRetrieved = getResult(future);
				trades.putAll(tradesRetrieved);
			} catch (InterruptedException | ExecutionException exception) {
				exception.printStackTrace();
			}
		}

		return trades;
	}

	private Map<Long, Trade> getResult(Future<Map<Long, Trade>> future)
			throws InterruptedException, ExecutionException {
		synchronized (TradesSiteRetriever.class) {
			Map<Long, Trade> result = future.get();
			shutDownExecutorService();
			return result;
		}

	}

	private void checkArguments(TimeInterval timeInterval) {
		if (timeInterval == null) {
			throw new IllegalArgumentException("Time interval cannot be null.");
		}
	}

	@Produces
	private PartialTradesSiteRetrieverCallable createPartialTradesSiteRetrieverCallable(Currency currency,
			TimeInterval timeInterval) {
		synchronized (TradesSiteRetriever.class) {
			createExecutorService();
			return new PartialTradesSiteRetrieverCallable(currency, timeInterval);
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
	}

	private static void createExecutorService() {
		if (executorService == null) {
			executorService = new ThreadPoolExecutor(configuredThreadPoolSize, configuredThreadPoolSize, 0l,
					TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		}
	}

	private static void shutDownExecutorService() {
		if (executorService != null && executorService.getActiveCount() == 0) {
			executorService.shutdownNow();
			executorService = null;
		}
	}
}
