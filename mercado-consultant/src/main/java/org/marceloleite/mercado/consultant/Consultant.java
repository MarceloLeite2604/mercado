package org.marceloleite.mercado.consultant;

import java.time.Duration;
import java.time.LocalDateTime;

import org.marceloleite.mercado.consultant.configuration.ConsultantProperty;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databaseretriever.persistence.dao.TradeDAO;
import org.marceloleite.mercado.properties.Property;
import org.marceloleite.mercado.retriever.PropertyRetriever;

public class Consultant {

	private static final Duration DEFAULT_TRADE_RETRIEVE_DURATION = Duration.ofSeconds(10);

	private static final Duration DEFAULT_TIME_INTERVAL_DURATION = Duration.ofSeconds(5);

	PropertyRetriever propertyRetriever;

	private LocalDateTime lastTimeRetrieved;

	private Duration tradeRetrieveDuration;

	private Duration timeInterval;

	private TradeDAO tradeDAO;
	
	private ConsultantThread consultantThread;

	public Consultant() {
		this.tradeDAO = new TradeDAO();
		this.propertyRetriever = new PropertyRetriever();
	}

	public void startConsulting() {
		retrieveConfiguration();
		consultLoop();
	}
	
	public void consultLoop() {
		consultantThread = new ConsultantThread(lastTimeRetrieved, tradeRetrieveDuration, timeInterval);
		consultantThread.start();
		try {
			consultantThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void retrieveConfiguration() {
		retrieveLastTimeRetrieved();
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

	private void retrieveLastTimeRetrieved() {
		TradePO newestTrade = tradeDAO.retrieveNewestTrade();
		if (null != newestTrade) {
			lastTimeRetrieved = newestTrade.getDate();
		} else {
			lastTimeRetrieved = LocalDateTime.of(2017, 01, 01, 0, 0);
		}
	}
}
