package org.marceloleite.mercado.retriever;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.converter.json.api.data.MapJsonTradeToListTradeConverter;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databaseretriever.persistence.dao.TradeDAO;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTrade;
import org.marceloleite.mercado.retriever.database.TradesDatabaseUtils;
import org.marceloleite.mercado.siteretriever.trades.TradesSiteRetriever;

public class TradesRetriever {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger(TradesRetriever.class);

	private static final boolean RETRIEVE_CACHED_TIME_INTERVAL_AVAILABLE = true;

	private TradeDAO tradeDAO;

	private Duration tradesSiteRetrieverStepDuration;

	public TradesRetriever() {
		this.tradeDAO = new TradeDAO();
		this.tradesSiteRetrieverStepDuration = null;
	}

	public void setTradesSiteRetrieverStepDuration(Duration tradesSiteRetrieverStepDuration) {
		this.tradesSiteRetrieverStepDuration = tradesSiteRetrieverStepDuration;
	}

	public List<TradePO> retrieve(Currency currency, TimeInterval timeInterval, boolean ignoreValuesFromDatabase) {
		return retrieve(currency, timeInterval.getStart(), timeInterval.getEnd(), ignoreValuesFromDatabase);
	}

	public List<TradePO> retrieve(Currency currency, ZonedDateTime start, ZonedDateTime end,
			boolean ignoreValuesFromDatabase) {

		if (!ignoreValuesFromDatabase) {
			retrieveAllValuesFromDatabase(currency, start, end);
		} else {
			List<TradePO> tradesRetrievedFromSite = retrieveTradesFromSite(currency, start, end);
			tradeDAO.merge(tradesRetrievedFromSite);
		}
		return retrieveTradesFromDatabase(currency, start, end);
	}

	private void retrieveAllValuesFromDatabase(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		List<TradePO> trades = retrieveTradesFromDatabase(currency, start, end);
		retrieveUnavailableTradesOnDatabase(currency, start, end);
		tradeDAO.merge(trades);
	}

	private List<TradePO> retrieveTradesFromDatabase(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		List<TradePO> trades = new TradeDAO().retrieve(currency, start, end);
		return trades;
	}

	private void retrieveUnavailableTradesOnDatabase(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		TimeInterval retrieveTimeIntervalAvailable = new TradesDatabaseUtils()
				.retrieveTimeIntervalAvailable(RETRIEVE_CACHED_TIME_INTERVAL_AVAILABLE);
		if (retrieveTimeIntervalAvailable != null) {
			if (start.isBefore(retrieveTimeIntervalAvailable.getStart())) {
				ZonedDateTime endRetrieveTime = ZonedDateTime.from(retrieveTimeIntervalAvailable.getStart())
						.minusSeconds(1);
				List<TradePO> trades = retrieveTradesFromSite(currency, start, endRetrieveTime);
				tradeDAO.merge(trades);
			}
			if (end.isAfter(retrieveTimeIntervalAvailable.getEnd())) {
				ZonedDateTime startRetrieveTime = ZonedDateTime.from(retrieveTimeIntervalAvailable.getEnd())
						.plusSeconds(1);
				List<TradePO> trades = retrieveTradesFromSite(currency, startRetrieveTime, end);
				tradeDAO.merge(trades);
			}
		} else {
			List<TradePO> trades = retrieveTradesFromSite(currency, start, end);
			tradeDAO.merge(trades);
		}
	}

	private List<TradePO> retrieveTradesFromSite(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		TimeInterval timeInterval = new TimeInterval(start, end);
		TradesSiteRetriever tradesSiteRetriever = new TradesSiteRetriever(currency);
		if (tradesSiteRetrieverStepDuration != null) {
			tradesSiteRetriever.setStepDuration(tradesSiteRetrieverStepDuration);
		}
		Map<Long, JsonTrade> jsonTrades = tradesSiteRetriever.retrieve(timeInterval);
		return new MapJsonTradeToListTradeConverter().convertTo(jsonTrades);
	}
}
