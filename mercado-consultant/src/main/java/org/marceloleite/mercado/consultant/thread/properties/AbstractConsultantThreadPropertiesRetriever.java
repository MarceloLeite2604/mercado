package org.marceloleite.mercado.consultant.thread.properties;

import java.time.Duration;
import java.time.LocalDateTime;

import org.marceloleite.mercado.commons.util.converter.StringToLocalDateTimeConverter;
import org.marceloleite.mercado.consultant.property.ConsultantProperty;
import org.marceloleite.mercado.databaseretriever.persistence.dao.TradeDAO;
import org.marceloleite.mercado.properties.Property;
import org.marceloleite.mercado.retriever.PropertyRetriever;

public abstract class AbstractConsultantThreadPropertiesRetriever implements ConsultantThreadPropertiesRetriever {

	protected static final LocalDateTime DEFAULT_START_TIME = LocalDateTime.of(2017, 01, 01, 0, 0);

	protected static final LocalDateTime OLDEST_SIMULATION_TIME = LocalDateTime.of(2015, 01, 01, 0, 0);

	protected static final Duration DEFAULT_TRADE_RETRIEVE_DURATION = Duration.ofSeconds(10);

	protected static final Duration DEFAULT_TIME_INTERVAL_DURATION = Duration.ofSeconds(5);

	protected static final boolean IGNORE_DATABASE_VALUE = false;

	private PropertyRetriever propertyRetriever;

	private TradeDAO tradeDAO;
	
	public PropertyRetriever getPropertyRetriever() {
		return propertyRetriever;
	}

	public TradeDAO getTradeDAO() {
		return tradeDAO;
	}

	@Override
	public ConsultantThreadProperties retrieveProperties() {
		LocalDateTime startTime = retrieveStartTime();
		LocalDateTime endTime = retrieveEndTime();
		Duration tradeRetrieveDuration = retrieveTradeRetrieveDuration();
		Duration timeInterval = retrieveTimeInterval();
		return new ConsultantThreadProperties(startTime, endTime, tradeRetrieveDuration, timeInterval);
	}

	protected abstract LocalDateTime retrieveStartTime();

	protected abstract LocalDateTime retrieveEndTime();

	protected abstract Duration retrieveTradeRetrieveDuration();

	protected abstract Duration retrieveTimeInterval();

	public AbstractConsultantThreadPropertiesRetriever() {
		this.propertyRetriever = new PropertyRetriever();
		this.tradeDAO = new TradeDAO();
	}

	protected Property retrieveProperty(ConsultantProperty consultantProperty) {
		return propertyRetriever.retrieve(consultantProperty,
				AbstractConsultantThreadPropertiesRetriever.IGNORE_DATABASE_VALUE);
	}

	protected Duration retrieveTimeIntervalFromProperty(ConsultantProperty consultantProperty) {
		Property timeIntervalProperty = retrieveProperty(consultantProperty);
		if (timeIntervalProperty.getValue() != null) {
			return Duration.ofSeconds(Long.parseLong(timeIntervalProperty.getValue()));
		} else {
			return null;
		}
	}

	protected Duration retrieveDurationFromProperties(ConsultantProperty consultantProperty, Duration defaultDuration) {
		Property tradeRetrieveDurationProperty = retrieveProperty(consultantProperty);
		if (tradeRetrieveDurationProperty.getValue() != null) {
			return Duration.ofSeconds(Long.parseLong(tradeRetrieveDurationProperty.getValue()));
		} else {
			return defaultDuration;
		}
	}

	protected LocalDateTime retrieveLocalDateTimeFromProperty(ConsultantProperty consultantProperty,
			LocalDateTime defaultTime) {
		Property property = retrieveProperty(consultantProperty);
		if (property.getValue() != null) {
			return new StringToLocalDateTimeConverter().convert(property.getValue());
		} else {
			return defaultTime;
		}
	}
}
