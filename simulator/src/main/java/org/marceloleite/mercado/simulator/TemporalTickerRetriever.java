package org.marceloleite.mercado.simulator;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.dao.interfaces.TemporalTickerDAO;
import org.marceloleite.mercado.model.TemporalTicker;
import org.springframework.stereotype.Component;

@Component
public class TemporalTickerRetriever {

	@Inject
	@Named("TemporalTickerDatabaseDAO")
	private TemporalTickerDAO temporalTickerDAO;
	
	public TreeMap<TimeInterval, Map<Currency, TemporalTicker>> retrieveTemporalTickers(TimeDivisionController timeDivisionController) {
		TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickersByTimeInterval = new TreeMap<>();
		for (TimeInterval timeInterval : timeDivisionController.geTimeIntervals()) {
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
