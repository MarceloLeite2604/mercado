package org.marceloleite.mercado.simulator;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.dao.interfaces.TemporalTickerDAO;
import org.marceloleite.mercado.model.TemporalTicker;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class TemporalTickerRetrieverCallable implements Callable<TreeMap<TimeInterval, Map<Currency, TemporalTicker>>> {

	private static final Logger LOGGER = LogManager.getLogger(Simulator.class);

	private TimeDivisionController timeDivisionController;

	@Inject
	@Named("TemporalTickerDatabaseDAO")
	private TemporalTickerDAO temporalTickerDAO;
	
	public TemporalTickerRetrieverCallable() {
		super();
		LOGGER.debug("Creating instance.");
	}

	@Override
	public TreeMap<TimeInterval, Map<Currency, TemporalTicker>> call() throws Exception {
		LOGGER.debug("Executing thread.");
//		TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickersByTimeInterval = new TreeMap<>();
//		for (TimeInterval timeInterval : timeDivisionController.geTimeIntervals()) {
//			temporalTickersByTimeInterval.put(timeInterval, retrieveTemporalTickers(timeInterval));
//		}
//		return temporalTickersByTimeInterval;
		return null;
	}

	private Map<Currency, TemporalTicker> retrieveTemporalTickers(TimeInterval timeInterval) {
		Map<Currency, TemporalTicker> temporalTickers = new HashMap<>();
		for (Currency currency : Currency.values()) {
			// TODO Watch out with BGold.
			if (currency.isDigital() && currency != Currency.BGOLD) {
				TemporalTicker temporalTicker = temporalTickerDAO.findByCurrencyAndStartAndEnd(currency,
						timeInterval.getStart(), timeInterval.getEnd());
				temporalTickers.put(currency, temporalTicker);
			}
		}
		return temporalTickers;
	}

	public void setTimeDivisionController(TimeDivisionController timeDivisionController) {
		this.timeDivisionController = timeDivisionController;
	}
	
}
