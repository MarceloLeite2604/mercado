package org.marceloleite.mercado.retriever;

import java.time.LocalDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.converter.ListJsonTradeToListTradeConverter;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databaseretriever.persistence.dao.TradeDAO;
import org.marceloleite.mercado.retriever.database.TradesDatabaseUtils;
import org.marceloleite.mercado.siteretriever.model.JsonTrade;
import org.marceloleite.mercado.siteretriever.trades.TradesSiteRetriever;

public class TradesRetriever {
	
	private TradeDAO tradeDAO;
	
	public TradesRetriever() {
		this.tradeDAO = new TradeDAO();
	}

	public List<TradePO> retrieve(Currency currency, LocalDateTime start, LocalDateTime end) {
		
		retrieveUnavailableTradesOnDatabase(currency, start, end);
		return retrieveTradesFromDatabase(currency, start, end);
	}

	private List<TradePO> retrieveTradesFromDatabase(Currency currency, LocalDateTime start, LocalDateTime end) {
		List<TradePO> trades = new TradeDAO().retrieve(currency, start, end);
		System.out.println(trades.size());
		return trades;
	}

	private void retrieveUnavailableTradesOnDatabase(Currency currency, LocalDateTime start, LocalDateTime end) {
		TimeInterval retrieveTimeIntervalAvailable = new TradesDatabaseUtils().retrieveTimeIntervalAvailable();
		
		if ( retrieveTimeIntervalAvailable != null ) {
			if ( start.isBefore(retrieveTimeIntervalAvailable.getStart())) {
				LocalDateTime endRetrieveTime = LocalDateTime.from(retrieveTimeIntervalAvailable.getStart()).minusSeconds(1);
				List<TradePO> trades = retrieveTradesFromSite(currency, start, endRetrieveTime);
				tradeDAO.persist(trades);
			}
			if ( end.isAfter(retrieveTimeIntervalAvailable.getEnd())) {
				LocalDateTime startRetrieveTime = LocalDateTime.from(retrieveTimeIntervalAvailable.getEnd()).plusSeconds(1);
				List<TradePO> trades = retrieveTradesFromSite(currency, startRetrieveTime, end);
				tradeDAO.persist(trades);
			}
		} else {
			List<TradePO> trades = retrieveTradesFromSite(currency, start, end);
			tradeDAO.persist(trades);
		}
	}

	private List<TradePO> retrieveTradesFromSite(Currency currency, LocalDateTime start, LocalDateTime end) {
		TradesSiteRetriever tradesConsumer = new TradesSiteRetriever(currency);
		List<JsonTrade> jsonTrades = tradesConsumer.retrieve(start, end);
		return new ListJsonTradeToListTradeConverter().convert(jsonTrades);
	}
}
