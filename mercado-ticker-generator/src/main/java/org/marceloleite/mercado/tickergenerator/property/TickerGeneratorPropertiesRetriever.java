package org.marceloleite.mercado.tickergenerator.property;

import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.util.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.properties.Property;
import org.marceloleite.mercado.properties.StandardProperty;
import org.marceloleite.mercado.retriever.PropertyRetriever;

public class TickerGeneratorPropertiesRetriever {

	private static final boolean IGNORE_DATABASE_VALUE = true;
	
	private static final boolean IGNORE_TEMPORAL_TICKERS_ON_DATABASE = false;

	private static final String DEFAULT_START_TIME = "01/06/2013 00:00:00";

	private PropertyRetriever propertyRetriever;

	public TickerGeneratorPropertiesRetriever() {
		super();
		this.propertyRetriever = new PropertyRetriever();
	}

	public ZonedDateTime retrieveStartTime() {
		StandardProperty startTimeProperty = retrieveProperty(TickerGeneratorProperty.START_TIME, DEFAULT_START_TIME);
		return new ZonedDateTimeToStringConverter().convertFrom(startTimeProperty.getValue());
	}

	public ZonedDateTime retrieveEndTime() {
		StandardProperty endTimeProperty = retrieveProperty(TickerGeneratorProperty.END_TIME, null);
		String endTimePropertyValue = endTimeProperty.getValue();
		if (endTimePropertyValue == null) {
			return null;
		} else {
			return new ZonedDateTimeToStringConverter().convertFrom(endTimePropertyValue);
		}

	}

	public boolean retrieveIgnoreTemporalTickersOnDatabase() {
		StandardProperty ignoreTemporalTickersOnDatabaseProperty = retrieveProperty(
				TickerGeneratorProperty.IGNORE_TEMPORAL_TICKERS_ON_DATABASE, Boolean.toString(IGNORE_TEMPORAL_TICKERS_ON_DATABASE));
		return Boolean.parseBoolean(ignoreTemporalTickersOnDatabaseProperty.getValue());
	}

	private StandardProperty retrieveProperty(Property property, String defaultValue) {
		propertyRetriever = new PropertyRetriever();
		StandardProperty propertyRetrieved = propertyRetriever.retrieve(property, IGNORE_DATABASE_VALUE);
		if (propertyRetrieved.getValue() == null) {
			if (property.isRequired()) {
				throw new RuntimeException("Required property \"" + property.getName() + "\" not found.");
			} else {
				propertyRetrieved.setValue(defaultValue);
			}
		}
		return propertyRetrieved;
	}

}
