package org.marceloleite.mercado.retriever;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.converter.datamodel.ListToMapTradeDataModelConverter;
import org.marceloleite.mercado.converter.entity.ListTradePOToListTradeDataModelConverter;
import org.marceloleite.mercado.database.data.structure.TradeDataModel;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databaseretriever.persistence.dao.TradeDAO;
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

	public List<TradeDataModel> retrieve(Currency currency, TimeInterval timeInterval, boolean ignoreValuesFromDatabase) {
		return retrieve(currency, timeInterval.getStart(), timeInterval.getEnd(), ignoreValuesFromDatabase);
	}

	public List<TradeDataModel> retrieve(Currency currency, ZonedDateTime start, ZonedDateTime end,
			boolean ignoreValuesFromDatabase) {
		ListTradePOToListTradeDataModelConverter listTradePOToListTradeDataModelConverter = new ListTradePOToListTradeDataModelConverter();
		if (!ignoreValuesFromDatabase) {
			retrieveAllValuesFromDatabase(currency, start, end);
		} else {
			List<TradeDataModel> tradesRetrievedFromSite = retrieveTradesFromSite(currency, start, end);
			List<TradePO> tradePOs = listTradePOToListTradeDataModelConverter.convertFrom(tradesRetrievedFromSite);
			tradeDAO.merge(tradePOs);
		}
		return retrieveTradesFromDatabase(currency, start, end);
	}

	private void retrieveAllValuesFromDatabase(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		List<TradeDataModel> trades = retrieveTradesFromDatabase(currency, start, end);
		retrieveUnavailableTradesOnDatabase(currency, start, end);
		tradeDAO.merge(new ListTradePOToListTradeDataModelConverter().convertFrom(trades));
	}

	private List<TradeDataModel> retrieveTradesFromDatabase(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		List<TradePO> tradePOs = new TradeDAO().retrieve(currency, start, end);
		return new ListTradePOToListTradeDataModelConverter().convertTo(tradePOs);
	}

	private void retrieveUnavailableTradesOnDatabase(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		TimeInterval retrieveTimeIntervalAvailable = new TradesDatabaseUtils()
				.retrieveTimeIntervalAvailable(RETRIEVE_CACHED_TIME_INTERVAL_AVAILABLE);
		ListTradePOToListTradeDataModelConverter listTradePOToListTradeDataModelConverter = new ListTradePOToListTradeDataModelConverter();
		if (retrieveTimeIntervalAvailable != null) {
			if (start.isBefore(retrieveTimeIntervalAvailable.getStart())) {
				ZonedDateTime endRetrieveTime = ZonedDateTime.from(retrieveTimeIntervalAvailable.getStart())
						.minusSeconds(1);
				List<TradeDataModel> tradeDataModels = retrieveTradesFromSite(currency, start, endRetrieveTime);
				List<TradePO> tradePOs = listTradePOToListTradeDataModelConverter.convertFrom(tradeDataModels);
				tradeDAO.merge(tradePOs);
			}
			if (end.isAfter(retrieveTimeIntervalAvailable.getEnd())) {
				ZonedDateTime startRetrieveTime = ZonedDateTime.from(retrieveTimeIntervalAvailable.getEnd())
						.plusSeconds(1);
				List<TradeDataModel> tradeDataModels = retrieveTradesFromSite(currency, startRetrieveTime, end);
				List<TradePO> tradePOs = listTradePOToListTradeDataModelConverter.convertFrom(tradeDataModels);
				tradeDAO.merge(tradePOs);
			}
		} else {
			List<TradeDataModel> tradeDataModels = retrieveTradesFromSite(currency, start, end);
			List<TradePO> tradePOs = listTradePOToListTradeDataModelConverter.convertFrom(tradeDataModels);
			tradeDAO.merge(tradePOs);
		}
	}

	private List<TradeDataModel> retrieveTradesFromSite(Currency currency, ZonedDateTime start, ZonedDateTime end) {
		TimeInterval timeInterval = new TimeInterval(start, end);
		TradesSiteRetriever tradesSiteRetriever = new TradesSiteRetriever(currency);
		if (tradesSiteRetrieverStepDuration != null) {
			tradesSiteRetriever.setStepDuration(tradesSiteRetrieverStepDuration);
		}
		Map<Long, TradeDataModel> jsonTrades = tradesSiteRetriever.retrieve(timeInterval);
		return new ListToMapTradeDataModelConverter().convertFrom(jsonTrades);
	}
}
