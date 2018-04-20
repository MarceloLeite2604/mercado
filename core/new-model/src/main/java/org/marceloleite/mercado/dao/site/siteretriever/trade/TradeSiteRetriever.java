package org.marceloleite.mercado.dao.site.siteretriever.trade;

import java.time.Duration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Produces;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.dao.site.siteretriever.AbstractSiteRetriever;
import org.marceloleite.mercado.model.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TradeSiteRetriever extends AbstractSiteRetriever {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(TradeSiteRetriever.class);

	private static final Duration DEFAULT_DURATION_STEP = Duration.ofMinutes(30);

	private static final int DEFAULT_THREAD_POOL_SIZE = 4;

	private static Duration configuredStepDuration = DEFAULT_DURATION_STEP;

	private static int configuredThreadPoolSize = DEFAULT_THREAD_POOL_SIZE;

	private static ThreadPoolExecutor executorService;

	private Duration stepDuration;

	// private int threadPoolSize;

	public TradeSiteRetriever(Duration stepDuration) {
		super();
		this.stepDuration = stepDuration;
		// this.threadPoolSize = configuredThreadPoolSize;
	}

	public TradeSiteRetriever() {
		this(configuredStepDuration);
	}

	public List<Trade> retrieve(Currency currency, TimeInterval timeInterval) {
		Set<Future<List<Trade>>> futureSet = new HashSet<>();

		checkArguments(timeInterval);

		TimeDivisionController timeDivisionController = new TimeDivisionController(timeInterval, stepDuration);
		for (TimeInterval timeIntervalDivision : timeDivisionController.geTimeIntervals()) {
			Callable<List<Trade>> partialTradesSiteRetrieverCallable = createPartialTradesSiteRetrieverCallable(
					currency, timeIntervalDivision);
			futureSet.add(executorService.submit(partialTradesSiteRetrieverCallable));

		}

		List<Trade> trades = new LinkedList<>();
		for (Future<List<Trade>> future : futureSet) {
			try {
				trades.addAll(getResult(future));
			} catch (InterruptedException | ExecutionException exception) {
				exception.printStackTrace();
			}
		}

		return trades;
	}

	private List<Trade> getResult(Future<List<Trade>> future)
			throws InterruptedException, ExecutionException {
		synchronized (TradeSiteRetriever.class) {
			List<Trade> result = future.get();
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
	private PartialTradeSiteRetrieverCallable createPartialTradesSiteRetrieverCallable(Currency currency,
			TimeInterval timeInterval) {
		synchronized (TradeSiteRetriever.class) {
			createExecutorService();
			return new PartialTradeSiteRetrieverCallable(currency, timeInterval);
		}

	}

	@Override
	protected String getMethod() {
		throw new UnsupportedOperationException();
	}

	public static void setConfiguredStepDuration(Duration configuredStepDuration) {
		TradeSiteRetriever.configuredStepDuration = configuredStepDuration;
	}

	public static void setConfiguredThreadPoolSize(int configuredThreadPoolSize) {
		TradeSiteRetriever.configuredThreadPoolSize = configuredThreadPoolSize;
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
