package org.marceloleite.mercado.simulator;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.cdi.MercadoApplicationContextAware;
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

	private TemporalTickerDAO temporalTickerDAO;

	private TimeDivisionController timeDivisionController;

	public TemporalTickerRetrieverCallable(TimeDivisionController timeDivisionController) {
		super();
		this.temporalTickerDAO = MercadoApplicationContextAware.getBean(TemporalTickerDAO.class,
				"TemporalTickerDatabaseDAO");
	}

	@Override
	public TreeMap<TimeInterval, Map<Currency, TemporalTicker>> call() throws Exception {
		LOGGER.debug("Executing thread.");
		TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickersByTimeInterval = new TreeMap<>();
		for (TimeInterval timeInterval : timeDivisionController.getTimeIntervals()) {
			temporalTickersByTimeInterval.put(timeInterval, retrieveTemporalTickers(timeInterval));
		}
		return temporalTickersByTimeInterval;
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
}
