package org.marceloleite.mercado.controller;

import java.util.EnumMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.dao.interfaces.TemporalTickerDAO;
import org.marceloleite.mercado.model.TemporalTicker;
import org.springframework.stereotype.Component;

@Component
public class TemporalTickerRetriever {

	@Inject
	@Named("TemporalTickerDatabaseDAO")
	private TemporalTickerDAO temporalTickerDAO;

	public Map<Currency, TemporalTicker> retrieveFor(TimeInterval timeInterval) {
		Map<Currency, TemporalTicker> temporalTickers = new EnumMap<>(Currency.class);
		for (Currency currency : Currency.values()) {
			// TODO Watch out with BGOLD.
			if (currency.isDigital() && currency != Currency.BGOLD) {
				TemporalTicker temporalTicker = temporalTickerDAO.findByCurrencyAndStartAndEnd(currency,
						timeInterval.getStart(), timeInterval.getEnd());
				temporalTickers.put(currency, temporalTicker);
			}
		}
		return temporalTickers;
	}
}
