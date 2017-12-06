package org.marceloleite.mercado.business;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.marceloleite.mercado.model.json.JsonTrade;

public class TradesRetriever {
	
	private static final Duration DURATION_STEP = Duration.ofMinutes(30); 

	public List<JsonTrade> retrieve(int totalSteps, int timeWindow, int timeUnit) {
		
		Calendar to = Calendar.getInstance();
		Calendar from = ((Calendar) to.clone());
		from.add(timeUnit, -timeWindow);
		ExecutorService executorService = Executors.newFixedThreadPool(totalSteps);
		Set<Future<List<JsonTrade>>> futureSet = new HashSet<>();

		
		for (int step = 0; step < totalSteps; step++) {
			TradesRetrieverCallable tradesRetrieverCallable = new TradesRetrieverCallable(from, to);
			futureSet.add(executorService.submit(tradesRetrieverCallable));
			to = ((Calendar) to.clone());
			from = ((Calendar) from.clone());
			to.add(timeUnit, -timeWindow);
			from.add(timeUnit, -timeWindow);
		}
		
		List<JsonTrade> tradesList = new ArrayList<>();
		for (Future<List<JsonTrade>> future : futureSet ) {
			try {
				List<JsonTrade> jsonTrades = future.get();
				tradesList.addAll(jsonTrades);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		return tradesList;
	}
	
	public List<JsonTrade> retrieve(Duration duration) {
		long steps = (duration.getSeconds()/DURATION_STEP.getSeconds());
		// TODO: Conclude.
		return null;
	}
}
