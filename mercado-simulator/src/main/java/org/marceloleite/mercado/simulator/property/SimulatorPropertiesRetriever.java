package org.marceloleite.mercado.simulator.property;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.util.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.properties.Property;
import org.marceloleite.mercado.retriever.PropertyRetriever;

public class SimulatorPropertiesRetriever {

	private static final boolean IGNORE_DATABASE_VALUES = true;

	private static final Duration DEFAULT_STEP_TIME = Duration.ofSeconds(30);

	private PropertyRetriever propertyRetriever;

	public SimulatorPropertiesRetriever() {
		this.propertyRetriever = new PropertyRetriever();
	}

	public ZonedDateTime retrieveStartTime() {
		return retrieveZonedDateTimeProperty(SimulatorProperty.START_TIME);
	}

	public ZonedDateTime retrieveEndTime() {
		return retrieveZonedDateTimeProperty(SimulatorProperty.END_TIME);
	}

	public Duration retrieveStepDurationTime() {
		return retrieveDurationProperty(SimulatorProperty.STEP_DURATION, DEFAULT_STEP_TIME);
	}

	private Duration retrieveDurationProperty(Property property, Duration defaultValue) {
		Property retrievedProperty = retrieveProperty(property);
		if (retrievedProperty.getValue() == null) {
			return defaultValue;
		} else {
			return Duration.ofSeconds(Long.parseLong(retrievedProperty.getValue()));
		}
	}

	private ZonedDateTime retrieveZonedDateTimeProperty(Property property) {
		Property retrievedProperty = retrieveProperty(property);
		ZonedDateTimeToStringConverter zonedDateTimeToStringConverter = new ZonedDateTimeToStringConverter(); 
		return zonedDateTimeToStringConverter.convertFrom(retrievedProperty.getValue());
	}

	private Property retrieveProperty(Property property) {
		return propertyRetriever.retrieve(property, IGNORE_DATABASE_VALUES);
	}
}
