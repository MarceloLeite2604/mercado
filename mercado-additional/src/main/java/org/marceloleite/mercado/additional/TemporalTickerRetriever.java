package org.marceloleite.mercado.additional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.commons.util.converter.LongToLocalDateTimeConverter;
import org.marceloleite.mercado.modeler.persistence.model.TemporalTicker;

public class TemporalTickerRetriever {

	public List<TemporalTicker> retrieve(Currency currency, LocalDateTime from, LocalDateTime to,
			Duration stepDuration) {
		Set<Future<TemporalTicker>> futureSet = new HashSet<>();
		long totalSteps = calculateSteps(from, to, stepDuration);
		/*
		 * ExecutorService executorService = Executors.newFixedThreadPool((int)
		 * (totalSteps == 0 ? 1 : totalSteps));
		 */
		ExecutorService executorService = Executors.newFixedThreadPool(8);

		LocalDateTime fromStep = from;
		Duration nextStepDuration = calculateStepDuration(from, to, stepDuration);
		for (long step = 0; step < totalSteps; step++) {
			Callable<TemporalTicker> temporalTickersCallable = new TemporalTickersCallable(currency, fromStep,
					nextStepDuration);
			futureSet.add(executorService.submit(temporalTickersCallable));
			fromStep = LocalDateTime.from(fromStep)
				.plus(nextStepDuration);
			nextStepDuration = calculateStepDuration(fromStep, to, stepDuration);
			to = LocalDateTime.from(to)
				.plus(nextStepDuration);
		}

		List<TemporalTicker> temporalTickers = new ArrayList<>();

		for (Future<TemporalTicker> future : futureSet) {
			try {
				TemporalTicker temporalTicker = future.get();
				temporalTickers.add(temporalTicker);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		executorService.shutdownNow();

		Collections.sort(temporalTickers, new TemporalTickersComparator());

		adjustValues(temporalTickers);

		return temporalTickers;
	}

	private void adjustValues(List<TemporalTicker> temporalTickers) {
		TemporalTicker previousTemporalTicker = null;
		for (TemporalTicker temporalTicker : temporalTickers) {
			if (previousTemporalTicker != null) {
				if (temporalTicker.getOrders() == 0) {
					temporalTicker.setHigh(previousTemporalTicker.getHigh());
					temporalTicker.setLow(previousTemporalTicker.getLow());
					temporalTicker.setLast(previousTemporalTicker.getLast());
					temporalTicker.setBuy(previousTemporalTicker.getBuy());
					temporalTicker.setSell(previousTemporalTicker.getSell());
				}
			}
			previousTemporalTicker = temporalTicker;
		}
	}

	private long calculateSteps(LocalDateTime from, LocalDateTime to, Duration stepDuration) {
		Duration timeDuration = Duration.between(from, to);
		long totalSteps = (long) Math.ceil((double) timeDuration.getSeconds() / (double) stepDuration.getSeconds());
		totalSteps = (totalSteps == 0 ? 1 : totalSteps);
		return totalSteps;
	}

	private Duration calculateStepDuration(LocalDateTime from, LocalDateTime to, Duration stepDuration) {
		Duration remainingDuration = Duration.between(from, to);
		if (stepDuration.compareTo(remainingDuration) > 0) {
			return remainingDuration;
		} else {
			return stepDuration;
		}
	}
}
