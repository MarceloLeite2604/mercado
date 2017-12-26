package org.marceloleite.mercado.consultant;

import java.time.Duration;
import java.time.LocalDateTime;

import org.marceloleite.mercado.consultant.configuration.ConsultantProperty;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databaseretriever.persistence.dao.TradeDAO;
import org.marceloleite.mercado.properties.Property;
import org.marceloleite.mercado.retriever.PropertyRetriever;

public class Consultant {
	
	private static final LocalDateTime START_TIME = LocalDateTime.of(2017, 01, 01, 0, 0);

	private static final Duration DEFAULT_TRADE_RETRIEVE_DURATION = Duration.ofSeconds(10);

	private static final Duration DEFAULT_TIME_INTERVAL_DURATION = Duration.ofSeconds(5);

	PropertyRetriever propertyRetriever;

	private LocalDateTime newestTimeRetrieved;
	
	private LocalDateTime oldestTimeRetrieved;

	private Duration tradeRetrieveDuration;

	private Duration timeInterval;

	private TradeDAO tradeDAO;
	
	private ForwardConsultantThread forwardConsultantThread;

	private BackwardConsultantThread backwardConsultantThread;

	public Consultant() {
		this.tradeDAO = new TradeDAO();
		this.propertyRetriever = new PropertyRetriever();
	}

	public void startConsulting() {
		retrieveConfiguration();
		consultLoop();
	}
	
	public void consultLoop() {
		forwardConsultantThread = new ForwardConsultantThread(newestTimeRetrieved, tradeRetrieveDuration, timeInterval);
		backwardConsultantThread = new BackwardConsultantThread(oldestTimeRetrieved, tradeRetrieveDuration, timeInterval);
		forwardConsultantThread.start();
		backwardConsultantThread.start();
		try {
			forwardConsultantThread.join();
			backwardConsultantThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void retrieveConfiguration() {
		retrieveNewestTimeRetrieved();
		retrieveOldestTimeRetrieved();
		retrieveTradeRetrieveDuration();
		retrieveTimeInterval();
	}

	private void retrieveTimeInterval() {
		Property property = propertyRetriever.retrieve(ConsultantProperty.TIME_INTERVAL, false);
		if (propertyIsValid(property)) {
			timeInterval = new StringToDurationConverter().convert(property.getValue());
		} else {
			timeInterval = DEFAULT_TIME_INTERVAL_DURATION;
		}
	}

	private boolean propertyIsValid(Property property) {
		return null != property && null != property.getValue();
	}

	private void retrieveTradeRetrieveDuration() {
		Property property = propertyRetriever.retrieve(ConsultantProperty.TRADE_RETRIEVE_DURATION, false);
		if (propertyIsValid(property)) {
			tradeRetrieveDuration = new StringToDurationConverter().convert(property.getValue());
		} else {
			tradeRetrieveDuration = DEFAULT_TRADE_RETRIEVE_DURATION;
		}
	}

	private void retrieveNewestTimeRetrieved() {
		TradePO newestTrade = tradeDAO.retrieveNewestTrade();
		if (null != newestTrade) {
			newestTimeRetrieved = newestTrade.getDate();
		} else {
			newestTimeRetrieved = LocalDateTime.from(START_TIME);
		}
	}
	
	private void retrieveOldestTimeRetrieved() {
		TradePO newestTrade = tradeDAO.retrieveOldestTrade();
		if (null != newestTrade) {
			oldestTimeRetrieved = newestTrade.getDate();
		} else {
			oldestTimeRetrieved = LocalDateTime.from(START_TIME);
		}
	}
}
