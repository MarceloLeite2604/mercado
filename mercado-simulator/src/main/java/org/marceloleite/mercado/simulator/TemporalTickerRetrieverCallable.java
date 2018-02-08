package org.marceloleite.mercado.simulator;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.databasemodel.TemporalTickerPO;
import org.marceloleite.mercado.retriever.TemporalTickerRetriever;

public class TemporalTickerRetrieverCallable implements Callable<TreeMap<TimeInterval, Map<Currency, TemporalTickerPO>>> {
	
	private static final String THREAD_NAME = "TempTick Retriever";
	
	private TimeDivisionController timeDivisionController;
	
	public TemporalTickerRetrieverCallable(TimeDivisionController timeDivisionController) {
		this.timeDivisionController = timeDivisionController;
	}

	@Override
	public TreeMap<TimeInterval, Map<Currency, TemporalTickerPO>> call() throws Exception {
		return new TemporalTickerRetriever().bulkRetrieve(timeDivisionController);
	}

}
