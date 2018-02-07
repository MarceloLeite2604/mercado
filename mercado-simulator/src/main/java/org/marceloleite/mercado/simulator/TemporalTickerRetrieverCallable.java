package org.marceloleite.mercado.simulator;

import java.util.Map;
import java.util.concurrent.Callable;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.retriever.TemporalTickerRetriever;

public class TemporalTickerRetrieverCallable implements Callable<Map<TimeInterval, Map<Currency, TemporalTickerPO>>> {
	
	private static final String THREAD_NAME = "TempTick Retriever";
	
	private TimeDivisionController timeDivisionController;
	
	public TemporalTickerRetrieverCallable(TimeDivisionController timeDivisionController) {
		Thread.currentThread().setName(THREAD_NAME);
		this.timeDivisionController = timeDivisionController;
	}

	@Override
	public Map<TimeInterval, Map<Currency, TemporalTickerPO>> call() throws Exception {
		return new TemporalTickerRetriever().bulkRetrieve(timeDivisionController);
	}

}
