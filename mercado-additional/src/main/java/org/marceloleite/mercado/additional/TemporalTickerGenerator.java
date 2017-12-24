package org.marceloleite.mercado.additional;

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
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;

public class TemporalTickerGenerator {

	public List<TemporalTickerPO> generate(Currency currency, TimeDivisionController timeDivisionController) {
		Set<Future<TemporalTickerPO>> futureSet = new HashSet<>();
		ExecutorService executorService = Executors.newFixedThreadPool(8);

		for (long step = 0; step < timeDivisionController.getDivisions(); step++) {
			TimeInterval nextTimeInterval = timeDivisionController.getNextTimeInterval();
			Callable<TemporalTickerPO> temporalTickersCallable = new TemporalTickersCallable(currency, nextTimeInterval);
			futureSet.add(executorService.submit(temporalTickersCallable));
		}

		List<TemporalTickerPO> temporalTickers = new ArrayList<>();

		for (Future<TemporalTickerPO> future : futureSet) {
			try {
				TemporalTickerPO temporalTicker = future.get();
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

	private void adjustValues(List<TemporalTickerPO> temporalTickers) {
		TemporalTickerPO previousTemporalTicker = null;
		for (TemporalTickerPO temporalTicker : temporalTickers) {
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
}
