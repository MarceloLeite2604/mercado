package org.marceloleite.mercado.simulator.property;

import java.time.Duration;
import java.time.ZonedDateTime;

import org.marceloleite.mercado.commons.properties.AbstractEnumPropertiesReader;

public class SimulatorPropertiesRetriever extends AbstractEnumPropertiesReader<SimulatorProperty>{

	private static final String PROPERTIES_FILE_NAME = "simulator.properties";

	public SimulatorPropertiesRetriever() {
		super(SimulatorProperty.class, PROPERTIES_FILE_NAME);
		readConfiguration();
	}

	public ZonedDateTime retrieveStartTime() {
		return getZonedDateTimeProperty(SimulatorProperty.START_TIME);
	}

	public ZonedDateTime retrieveEndTime() {
		return getZonedDateTimeProperty(SimulatorProperty.END_TIME);
	}

	public Duration retrieveStepDurationTime() {
		return getDurationProperty(SimulatorProperty.STEP_DURATION);
	}
	
	public Duration retrieveRetrievingDurationTime() {
		return getDurationProperty(SimulatorProperty.RETRIEVING_DURATION);
	}
	
	public String retrievePersistencePropertyFile() {
		return getProperty(SimulatorProperty.PERSISTENCE_FILE).getValue();
	}
}
