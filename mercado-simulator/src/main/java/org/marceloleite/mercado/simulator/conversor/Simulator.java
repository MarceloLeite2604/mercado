package org.marceloleite.mercado.simulator.conversor;

import java.time.Duration;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.converter.DurationToStringConverter;
import org.marceloleite.mercado.commons.util.converter.LocalDateTimeToStringConverter;
import org.marceloleite.mercado.simulator.property.SimulatorPropertiesRetriever;

public class Simulator {

	private static final Logger LOGGER = LogManager.getLogger(Simulator.class);

	private House house;

	private TimeDivisionController timeDivisionController;

	public Simulator() {
		this.house = new House();
	}

	private void configure() {
		SimulatorPropertiesRetriever simulatorPropertiesRetriever = new SimulatorPropertiesRetriever();
		LocalDateTime startTime = simulatorPropertiesRetriever.retrieveStartTime();
		LocalDateTime endTime = simulatorPropertiesRetriever.retrieveEndTime();
		Duration stepTime = simulatorPropertiesRetriever.retrieveStepDurationTime();
		timeDivisionController = new TimeDivisionController(startTime, endTime, stepTime);
		this.house = new House();
	}

	public void runSimulation() {
		LOGGER.traceEntry();

		configure();
		logSimulationStart();

		for (TimeInterval timeInterval : timeDivisionController.geTimeIntervals()) {
			house.executeTemporalEvents(timeInterval);
		}

		LOGGER.info("Simulation finished.");
	}

	private void logSimulationStart() {
		LocalDateTimeToStringConverter localDateTimeToStringConverter = new LocalDateTimeToStringConverter();
		DurationToStringConverter durationToStringConverter = new DurationToStringConverter();
		LOGGER.info("Starting simulation from "
				+ localDateTimeToStringConverter.convertTo(timeDivisionController.getStart()) + " to "
				+ localDateTimeToStringConverter.convertTo(timeDivisionController.getEnd()) + " with a step of "
				+ durationToStringConverter.convertTo(timeDivisionController.getDivisionDuration()) + ".");
	}
}
