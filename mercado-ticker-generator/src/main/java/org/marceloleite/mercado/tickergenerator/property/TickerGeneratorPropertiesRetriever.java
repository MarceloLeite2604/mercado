package org.marceloleite.mercado.tickergenerator.property;

import java.time.Duration;
import java.time.LocalDateTime;

import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.util.converter.StringToLocalDateTimeConverter;
import org.marceloleite.mercado.properties.Property;
import org.marceloleite.mercado.properties.StandardProperty;
import org.marceloleite.mercado.retriever.PropertyRetriever;

public class TickerGeneratorPropertiesRetriever {

	private static final boolean IGNORE_DATABASE_VALUE = true;

	private PropertyRetriever propertyRetriever;

	public TickerGeneratorPropertiesRetriever() {
		super();
		this.propertyRetriever = new PropertyRetriever();
	}

	public TimeDivisionController retrieveTimeDivisionController() {
		StringToLocalDateTimeConverter stringToLocalDateTimeConverter = new StringToLocalDateTimeConverter();
		StandardProperty startTimeProperty = retrieveProperty(TickerGeneratorProperty.START_TIME);
		LocalDateTime startTime = stringToLocalDateTimeConverter.convert(startTimeProperty.getValue());
		StandardProperty endTimeProperty = retrieveProperty(TickerGeneratorProperty.END_TIME);
		LocalDateTime endTime = stringToLocalDateTimeConverter.convert(endTimeProperty.getValue());
		StandardProperty stepDurationProperty = retrieveProperty(TickerGeneratorProperty.STEP_DURATION);
		Duration stepDuration = Duration.ofSeconds(Long.parseLong(stepDurationProperty.getValue()));
		return new TimeDivisionController(startTime, endTime, stepDuration);
	}

	public boolean retrieveIgnoreTemporalTickersOnDatabase() {
		StandardProperty ignoreTemporalTickersOnDatabaseProperty = retrieveProperty(
				TickerGeneratorProperty.IGNORE_TEMPORAL_TICKERS_ON_DATABASE);
		if (ignoreTemporalTickersOnDatabaseProperty == null) {
			return false;
		} else {
			return Boolean.parseBoolean(ignoreTemporalTickersOnDatabaseProperty.getValue());
		}
	}

	private StandardProperty retrieveProperty(Property property) {
		propertyRetriever = new PropertyRetriever();
		StandardProperty propertyRetrieved = propertyRetriever.retrieve(property, IGNORE_DATABASE_VALUE);
		if (propertyRetrieved.getValue() == null && property.isRequired()) {
			throw new RuntimeException("Required property \"" + property.getName() + "\" not found.");
		}
		return propertyRetrieved;
	}

}
