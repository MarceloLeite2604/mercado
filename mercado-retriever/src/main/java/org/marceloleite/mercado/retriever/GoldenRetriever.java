package org.marceloleite.mercado.retriever;

import java.time.LocalDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.modeler.persistence.model.Trade;

public class GoldenRetriever {

	public void retrieve(Currency currency, LocalDateTime start, LocalDateTime end) {
		
		DatabaseTradesRetriever databaseTradesRetriever = new DatabaseTradesRetriever();
		TimeInterval retrieveTimeIntervalAvailable = databaseTradesRetriever.retrieveTimeIntervalAvailable();
		
		TradeDAO tradeDAO = new TradeDAO();
		
		if ( retrieveTimeIntervalAvailable != null ) {
			if ( start.isBefore(retrieveTimeIntervalAvailable.getStart())) {
				LocalDateTime endRetrieveTime = LocalDateTime.from(retrieveTimeIntervalAvailable.getStart()).minusSeconds(1);
				List<Trade> trades = retrieveTradesFromSite(currency, start, endRetrieveTime);
				
			}
			if ( end.isAfter(retrieveTimeIntervalAvailable.getEnd())) {
				LocalDateTime startRetrieveTime = LocalDateTime.from(retrieveTimeIntervalAvailable.getEnd()).plusSeconds(1);
				List<Trade> trades = retrieveTradesFromSite(currency, startRetrieveTime, end);
			}
		} else {
			List<Trade> trades = retrieveTradesFromSite(currency, start, end);
		}
	}
}
