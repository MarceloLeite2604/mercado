package org.marceloleite.mercado.retriever;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.converter.entity.ListTradePOToListTradeConverter;
import org.marceloleite.mercado.data.Trade;
import org.marceloleite.mercado.databaseretriever.persistence.daos.TradeDAO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TradePO;
import org.marceloleite.mercado.retriever.database.TradesDatabaseUtils;
import org.marceloleite.mercado.siteretriever.converters.ListToMapTradeConverter;
import org.marceloleite.mercado.siteretriever.trades.TradesSiteRetriever;

public class TradesRetriever {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(TradesRetriever.class);

	private static final boolean RETRIEVE_CACHED_TIME_INTERVAL_AVAILABLE = true;

	private TradeDAO tradeDAO;

	public TradesRetriever() {
		this.tradeDAO = new TradeDAO(); 
	}

	public List<Trade> retrieve(Currency currency, TimeInterval timeInterval, boolean ignoreValuesFromDatabase) {
		return retrieve(currency, timeInterval.getStart(), timeInterval.getEnd(), ignoreValuesFromDatabase);
	}

	public List<Trade> retrieve(Currency currency, ZonedDateTime start, ZonedDateTime end,
			boolean ignoreValuesFromDatabase) {
		ListTradePOToListTradeConverter listTradePOToListTradeConverter = new ListTradePOToListTradeConverter();
		if (!ignoreValuesFromDatabase) {
			retrieveAllValuesFromDatabase(currency, start, end);
		} else {
			List<Trade> tradesRetrievedFromSite = retrieveTradesFromSite(currency, start, end);
			List<TradePO> tradePOs = listTradePOToListTradeConverter.convertFrom(tradesRetrievedFromSite);
			tradeDAO.merge(tradePOs);
		}
		return retrieveTradesFromDatabase(currency, start, end);
	}

	private void retrieveAllValuesFromDatabase(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		List<Trade> trades = retrieveTradesFromDatabase(currency, start, end);
		retrieveUnavailableTradesOnDatabase(currency, start, end);
		tradeDAO.merge(new ListTradePOToListTradeConverter().convertFrom(trades));
	}

	private List<Trade> retrieveTradesFromDatabase(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		List<TradePO> tradePOs = new TradeDAO().retrieve(currency, start, end);
		return new ListTradePOToListTradeConverter().convertTo(tradePOs);
	}

	private void retrieveUnavailableTradesOnDatabase(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		TimeInterval retrieveTimeIntervalAvailable = new TradesDatabaseUtils()
				.retrieveTimeIntervalAvailable(RETRIEVE_CACHED_TIME_INTERVAL_AVAILABLE);
		ListTradePOToListTradeConverter listTradePOToListTradeConverter = new ListTradePOToListTradeConverter();
		if (retrieveTimeIntervalAvailable != null) {
			if (start.isBefore(retrieveTimeIntervalAvailable.getStart())) {
				ZonedDateTime endRetrieveTime = ZonedDateTime.from(retrieveTimeIntervalAvailable.getStart())
						.minusSeconds(1);
				List<Trade> trades = retrieveTradesFromSite(currency, start, endRetrieveTime);
				List<TradePO> tradePOs = listTradePOToListTradeConverter.convertFrom(trades);
				tradeDAO.merge(tradePOs);
			}
			if (end.isAfter(retrieveTimeIntervalAvailable.getEnd())) {
				ZonedDateTime startRetrieveTime = ZonedDateTime.from(retrieveTimeIntervalAvailable.getEnd())
						.plusSeconds(1);
				List<Trade> trades = retrieveTradesFromSite(currency, startRetrieveTime, end);
				List<TradePO> tradePOs = listTradePOToListTradeConverter.convertFrom(trades);
				tradeDAO.merge(tradePOs);
			}
		} else {
			List<Trade> trades = retrieveTradesFromSite(currency, start, end);
			List<TradePO> tradePOs = listTradePOToListTradeConverter.convertFrom(trades);
			tradeDAO.merge(tradePOs);
		}
	}

	private List<Trade> retrieveTradesFromSite(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		TimeInterval timeInterval = new TimeInterval(start, end);
		
		TradesSiteRetriever tradesSiteRetriever = new TradesSiteRetriever(currency);
		Map<Long, Trade> jsonTrades = tradesSiteRetriever.retrieve(timeInterval);
		return new ListToMapTradeConverter().convertFrom(jsonTrades);
	}
}
