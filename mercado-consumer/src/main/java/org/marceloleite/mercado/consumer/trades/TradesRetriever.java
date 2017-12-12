package org.marceloleite.mercado.consumer.trades;

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

import org.marceloleite.mercado.consumer.model.Currency;
import org.marceloleite.mercado.consumer.model.JsonTrade;

public class TradesRetriever {

	private static final Duration DEFAULT_DURATION_STEP = Duration.ofMinutes(30);

	private Duration stepDuration;

	public TradesRetriever() {
		this.stepDuration = DEFAULT_DURATION_STEP;
	}

	public TradesRetriever(Duration stepDuration) {
		super();
		this.stepDuration = stepDuration;
	}

	public void setStepDuration(Duration stepDuration) {
		this.stepDuration = stepDuration;
	}

	public Map<Integer, JsonTrade> retrieve(Currency cryptocoin, LocalDateTime initialTime, Duration duration) {
		Set<Future<Map<Integer, JsonTrade>>> futureSet = new HashSet<>();
		long totalSteps = (duration.getSeconds() / stepDuration.getSeconds());
		totalSteps = (totalSteps == 0 ? 1 : totalSteps);
		ExecutorService executorService = Executors.newFixedThreadPool((int) (totalSteps == 0 ? 1 : totalSteps));

		LocalDateTime from = initialTime;
		Duration nextStepDuration = calculateStepDuration(from, initialTime, duration);
		LocalDateTime to = LocalDateTime.from(initialTime).plus(nextStepDuration);

		for (int step = 0; step < totalSteps; step++) {
			Callable<Map<Integer, JsonTrade>> tradesRetrieverCallable = new TradesRetrieverCallable(cryptocoin, from,
					to);
			futureSet.add(executorService.submit(tradesRetrieverCallable));
			from = LocalDateTime.from(from).plus(nextStepDuration);
			nextStepDuration = calculateStepDuration(from, initialTime, duration);
			to = LocalDateTime.from(to).plus(nextStepDuration);
		}

		Map<Integer, JsonTrade> trades = new ConcurrentHashMap<>();
		for (Future<Map<Integer, JsonTrade>> future : futureSet) {
			try {
				Map<Integer, JsonTrade> jsonTrades = future.get();
				trades.putAll(jsonTrades);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		executorService.shutdownNow();

		return trades;
	}

	public Duration calculateStepDuration(LocalDateTime stepStartTime, LocalDateTime initialTime, Duration duration) {
		Duration remainingDuration = Duration.between(stepStartTime, initialTime.plus(duration));
		if (stepDuration.compareTo(remainingDuration) > 0) {
			return remainingDuration;
		} else {
			return stepDuration;
		}
	}
}
