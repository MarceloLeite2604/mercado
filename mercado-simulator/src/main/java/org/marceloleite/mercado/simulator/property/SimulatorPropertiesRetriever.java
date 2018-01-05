package org.marceloleite.mercado.simulator.property;

import java.time.Duration;
import java.time.LocalDateTime;

import org.marceloleite.mercado.commons.util.converter.StringToLocalDateTimeConverter;
import org.marceloleite.mercado.properties.Property;
import org.marceloleite.mercado.retriever.PropertyRetriever;

public class SimulatorPropertiesRetriever {

	private static final boolean IGNORE_DATABASE_VALUES = true;

	private static final Duration DEFAULT_STEP_TIME = Duration.ofSeconds(30);

	private PropertyRetriever propertyRetriever;

	public SimulatorPropertiesRetriever() {
		this.propertyRetriever = new PropertyRetriever();
	}

	public LocalDateTime retrieveStartTime() {
		return retrieveLocalDateTimeProperty(SimulatorProperty.START_TIME);
	}

	public LocalDateTime retrieveEndTime() {
		return retrieveLocalDateTimeProperty(SimulatorProperty.END_TIME);
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

	private LocalDateTime retrieveLocalDateTimeProperty(Property property) {
		Property retrievedProperty = retrieveProperty(property);
		StringToLocalDateTimeConverter stringToLocalDateTimeConverter = new StringToLocalDateTimeConverter();
		return stringToLocalDateTimeConverter.convertTo(retrievedProperty.getValue());
	}

	private Property retrieveProperty(Property property) {
		return propertyRetriever.retrieve(property, IGNORE_DATABASE_VALUES);
	}
}
