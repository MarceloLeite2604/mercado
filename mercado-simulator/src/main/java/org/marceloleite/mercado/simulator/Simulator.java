package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.converter.TimeDivisionControllerToStringConverter;
import org.marceloleite.mercado.commons.util.converter.TimeIntervalToStringConverter;
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
			logSimulationStep(timeInterval);
			house.executeTemporalEvents(timeInterval);
		}

		LOGGER.info("Simulation finished.");
	}

	private void logSimulationStep(TimeInterval timeInterval) {
		TimeIntervalToStringConverter timeIntervalToStringConverter = new TimeIntervalToStringConverter(); 
		LOGGER.info("Advancing to step time " + timeIntervalToStringConverter.convertTo(timeInterval) + "."); 
	}

	private void logSimulationStart() {
		TimeDivisionControllerToStringConverter timeDivisionControllerToStringConverter = new TimeDivisionControllerToStringConverter();
		LOGGER.info("Starting simulation from "
				+ timeDivisionControllerToStringConverter.convertTo(timeDivisionController) + ".");
	}
}
