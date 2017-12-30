package org.marceloleite.mercado.tickergenerator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.retriever.TradesRetriever;

public class TickerCreator {
	
	private static final boolean IGNORE_VALUES_FROM_DATABASE = false;

	public void create(LocalDateTime start, LocalDateTime end, Duration duration) {
		TradesRetriever tradesRetriever = new TradesRetriever();
		TimeDivisionController timeDivisionController = new TimeDivisionController(start, end, duration);
		for(long counter = 0; counter < timeDivisionController.getDivisions(); counter++) {
			
			for (Currency currency : Currency.values()) {
				if (currency.isDigital()) {
						List<TradePO> trades = tradesRetriever.retrieve(currency, start, end, IGNORE_VALUES_FROM_DATABASE);
				}
			}
		}
	}

}
