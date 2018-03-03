package org.marceloleite.mercado.consultant.thread.property;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.consultant.property.ConsultantProperty;
import org.marceloleite.mercado.databaseretriever.persistence.daos.TradeDAO;
import org.marceloleite.mercado.retriever.PropertyRetriever;

public abstract class AbstractConsultantThreadPropertiesRetriever implements ConsultantThreadPropertiesRetriever {

	protected static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.of(2017, 01, 01, 0, 0, 0, 0, ZoneOffset.UTC);

	protected static final ZonedDateTime OLDEST_SIMULATION_TIME = ZonedDateTime.of(2013, 06, 01, 0, 0, 0, 0, ZoneOffset.UTC);

	protected static final Duration DEFAULT_TRADE_RETRIEVE_DURATION = Duration.ofSeconds(10);

	protected static final Duration DEFAULT_TIME_INTERVAL_DURATION = Duration.ofSeconds(5);

	protected static final boolean IGNORE_DATABASE_PARAMETERS = true;

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
		ZonedDateTime startTime = retrieveStartTime();
		ZonedDateTime endTime = retrieveEndTime();
		Duration tradeRetrieveDuration = retrieveTradeRetrieveDuration();
		Duration timeInterval = retrieveTimeInterval();
		boolean databaseValuesIgnored = retrieveDatabaseValuesIgnored();
		Duration tradesSiteRetrieverStepDuration = retrieveTradesSiteRetrieverStepDuration();
		return new ConsultantThreadProperties(startTime, endTime, tradeRetrieveDuration, timeInterval,
				databaseValuesIgnored, tradesSiteRetrieverStepDuration);
	}

	protected abstract ZonedDateTime retrieveStartTime();

	protected abstract ZonedDateTime retrieveEndTime();

	protected abstract Duration retrieveTradeRetrieveDuration();

	protected abstract Duration retrieveTimeInterval();

	protected abstract boolean retrieveDatabaseValuesIgnored();
	
	protected abstract Duration retrieveTradesSiteRetrieverStepDuration();

	public AbstractConsultantThreadPropertiesRetriever() {
		this.propertyRetriever = new PropertyRetriever();
		this.tradeDAO = new TradeDAO();
	}

	protected Property retrieveProperty(ConsultantProperty consultantProperty) {
		return propertyRetriever.retrieve(consultantProperty,
				AbstractConsultantThreadPropertiesRetriever.IGNORE_DATABASE_PARAMETERS);
	}

	protected Duration retrieveTimeIntervalProperty(ConsultantProperty consultantProperty) {
		Property timeIntervalProperty = retrieveProperty(consultantProperty);
		if (timeIntervalProperty.getValue() != null) {
			return Duration.ofSeconds(Long.parseLong(timeIntervalProperty.getValue()));
		} else {
			return null;
		}
	}

	protected Duration retrieveDurationProperty(ConsultantProperty consultantProperty, Duration defaultDuration) {
		Property tradeRetrieveDurationProperty = retrieveProperty(consultantProperty);
		if (tradeRetrieveDurationProperty.getValue() != null) {
			return Duration.ofSeconds(Long.parseLong(tradeRetrieveDurationProperty.getValue()));
		} else {
			return defaultDuration;
		}
	}

	protected ZonedDateTime retrieveZonedDateTimeProperty(ConsultantProperty consultantProperty,
			ZonedDateTime defaultValue) {
		Property property = retrieveProperty(consultantProperty);
		if (property.getValue() != null) {
			return new ZonedDateTimeToStringConverter().convertFrom(property.getValue());
		} else {
			return defaultValue;
		}
	}
	
	protected boolean retrieveBooleanProperty(ConsultantProperty consultantProperty, boolean defaultValue) {
		Property property = retrieveProperty(consultantProperty);
		if (property.getValue() != null) {
			return Boolean.parseBoolean(property.getValue());
		} else {
			return defaultValue;
		}
	}
}
