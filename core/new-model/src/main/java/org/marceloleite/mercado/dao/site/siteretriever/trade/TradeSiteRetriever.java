package org.marceloleite.mercado.dao.site.siteretriever.trade;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.dao.site.siteretriever.AbstractSiteRetriever;
import org.marceloleite.mercado.model.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

@Component
public class TradeSiteRetriever extends AbstractSiteRetriever {

	private static final Logger LOGGER = LoggerFactory.getLogger(TradeSiteRetriever.class);

	private static final Duration DEFAULT_DURATION_STEP = Duration.ofMinutes(720);

	private static Duration configuredStepDuration = DEFAULT_DURATION_STEP;

	private Duration stepDuration;

	public TradeSiteRetriever(Duration stepDuration) {
		super();
		this.stepDuration = stepDuration;
	}

	public TradeSiteRetriever() {
		this(configuredStepDuration);
	}

	public List<Trade> retrieve(Currency currency, TimeInterval timeInterval) {

		checkArguments(currency, timeInterval);

		List<Future<TSRResult>> futures = create(currency, timeInterval);

		// monitor
		List<Trade> result = monitor(currency, timeInterval, futures);

		return result;
	}

	private List<Trade> monitor(Currency currency, TimeInterval timeInterval, List<Future<TSRResult>> futures) {

		List<Trade> result = new ArrayList<>();

		int counter = 0;
		boolean finished = false;

		while (!finished) {
			Future<TSRResult> future = futures.get(counter);

			if (future.isDone()) {
				// LOGGER.debug("Thread " + counter + " is done.");
				TSRResult tsrResult = retrieveResult(currency, timeInterval, future);
				futures.remove(counter);
				switch (tsrResult.getTsrResultStatus()) {
				case OK:
					result.addAll(tsrResult.getTrades());
					break;
				case MAX_TRADES_REACHED:
					split(futures, tsrResult);
					break;
				case FAILURE:
					abort(futures);
					throw createRuntimeException(currency, timeInterval, tsrResult.getException());
				}
			}

			if (futures.size() == 0) {
				finished = true;
			} else {
				counter = (++counter % futures.size());
			}
		}
		
		result = adjustCurrencies(currency, result);

		return result;
	}

	private List<Trade> adjustCurrencies(Currency currency, List<Trade> trades) {
		trades.stream().forEach(trade -> trade.setCurrency(currency));
		return trades;
	}

	private TSRResult retrieveResult(Currency currency, TimeInterval timeInterval, Future<TSRResult> future) {
		TSRResult tsrResult;
		try {
			tsrResult = future.get();
		} catch (InterruptedException | ExecutionException exception) {
			throw createRuntimeException(currency, timeInterval, exception);
		}
		return tsrResult;
	}

	private List<Future<TSRResult>> create(Currency currency, TimeInterval timeInterval) {
		List<Future<TSRResult>> result = new LinkedList<>();
		TimeDivisionController timeDivisionController = new TimeDivisionController(timeInterval, stepDuration);
		for (TimeInterval divisionTimeInterval : timeDivisionController.getTimeIntervals()) {
			// LOGGER.debug("Created thread to retrieve " + currency + " currency for" + divisionTimeInterval + ".");
			result.add(createFuture(currency, divisionTimeInterval));
		}

		return result;
	}

	private RuntimeException createRuntimeException(Currency currency, TimeInterval timeInterval, Exception exception) {
		return new RuntimeException(
				"Error while retrieving trades for " + currency + " currency for period " + timeInterval + ".",
				exception);
	}

	private void abort(List<Future<TSRResult>> futures) {
		for (Future<TSRResult> future : futures) {
			future.cancel(true);
		}
	}

	private void split(List<Future<TSRResult>> futures, TSRResult tsrResult) {
		// LOGGER.debug("Splitting execution.");
		TimeInterval timeInterval = tsrResult.getTsrParameters()
				.getTimeInterval();
		Currency currency = tsrResult.getTsrParameters()
				.getCurrency();
		Duration dividedDuration = timeInterval.getDuration()
				.dividedBy(2);
		TimeInterval firstTimeInterval = new TimeInterval(timeInterval.getStart(), dividedDuration);
		TimeInterval secondTimeInterval = new TimeInterval(timeInterval.getStart()
				.plus(dividedDuration), timeInterval.getEnd());
		futures.add(createFuture(currency, firstTimeInterval));
		futures.add(createFuture(currency, secondTimeInterval));
	}

	@Async
	private Future<TSRResult> createFuture(Currency currency, TimeInterval timeInterval) {
		return new AsyncResult<>(new TSRPartial().retrieve(new TSRParameters(currency, timeInterval)));
	}

	private void checkArguments(Currency currency, TimeInterval timeInterval) {
		if (currency == null) {
			throw new IllegalArgumentException("Currency cannot be null.");
		}
		if (timeInterval == null) {
			throw new IllegalArgumentException("Time interval cannot be null.");
		}
	}

	@Override
	protected String getMethod() {
		throw new UnsupportedOperationException();
	}

	public static void setConfiguredStepDuration(Duration configuredStepDuration) {
		TradeSiteRetriever.configuredStepDuration = configuredStepDuration;
	}
}
