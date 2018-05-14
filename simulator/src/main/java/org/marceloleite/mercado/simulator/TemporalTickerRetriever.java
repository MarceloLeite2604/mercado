package org.marceloleite.mercado.simulator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.dao.interfaces.TemporalTickerDAO;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.simulator.conversor.TemporalTickersByCurrencyMapToTemporalTickersByTimeIntervalMapConversor;
import org.springframework.stereotype.Component;

@Component
public class TemporalTickerRetriever {

	@Inject
	@Named("TemporalTickerDatabaseDAO")
	private TemporalTickerDAO temporalTickerDAO;

	public TreeMap<TimeInterval, Map<Currency, TemporalTicker>> retrieveTemporalTickers(
			TimeDivisionController timeDivisionController) {
		TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickersByTimeInterval;
		temporalTickersByTimeInterval = retrieveAllTemporalTickersForTimeDivisionController(timeDivisionController);
		if (temporalTickersByTimeInterval == null) {
			temporalTickersByTimeInterval = retrieveEachTemporalTickerForTimeDivisionController(timeDivisionController);
		}
		return temporalTickersByTimeInterval;
	}

	private TreeMap<TimeInterval, Map<Currency, TemporalTicker>> retrieveEachTemporalTickerForTimeDivisionController(
			TimeDivisionController timeDivisionController) {
		TreeMap<TimeInterval, Map<Currency, TemporalTicker>> temporalTickersByTimeInterval;
		temporalTickersByTimeInterval = new TreeMap<>();
		for (TimeInterval timeInterval : timeDivisionController.getTimeIntervals()) {
			temporalTickersByTimeInterval.put(timeInterval, retrieveTemporalTickers(timeInterval));
		}
		return temporalTickersByTimeInterval;
	}

	private TreeMap<TimeInterval, Map<Currency, TemporalTicker>> retrieveAllTemporalTickersForTimeDivisionController(
			TimeDivisionController timeDivisionController) {
		TreeMap<TimeInterval, Map<Currency, TemporalTicker>> result;
		Map<Currency, List<TemporalTicker>> temporalTickersByCurrency = new HashMap<>();
		Currency[] currencyValues = Currency.values();
		boolean finished = false;
		boolean success = true;
		int counter = 0;
		Currency currency = currencyValues[counter];
		while (!finished) {

			// TODO Watch out with BGOLD.
			if (currency.isDigital() && currency != Currency.BGOLD) {
				List<TemporalTicker> temporalTickers = temporalTickerDAO.findByCurrencyAndDurationAndStartBetween(
						currency, timeDivisionController.getDivisionDuration(),
						timeDivisionController.getTotalTimeInterval());
				if (hasAllTemporalTickersForTimeDivisionDuration(temporalTickers, timeDivisionController)) {
					temporalTickersByCurrency.put(currency, temporalTickers);
				} else {
					success = false;
					finished = true;
				}
			}

			if (++counter < currencyValues.length) {
				currency = currencyValues[counter];
			} else {
				finished = true;
			}
		}

		if (success) {
			result = pivotTemporalTickersByTimeInterval(temporalTickersByCurrency);
		} else {
			result = null;
		}
		return result;
	}

	private TreeMap<TimeInterval, Map<Currency, TemporalTicker>> pivotTemporalTickersByTimeInterval(
			Map<Currency, List<TemporalTicker>> temporalTickersByCurrency) {
		return TemporalTickersByCurrencyMapToTemporalTickersByTimeIntervalMapConversor
				.convert(temporalTickersByCurrency);
	}

	private boolean hasAllTemporalTickersForTimeDivisionDuration(List<TemporalTicker> temporalTickers,
			TimeDivisionController timeDivisionController) {
		return (temporalTickers != null && temporalTickers.size() == timeDivisionController.getTotalDivisions());
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
