package org.marceloleite.mercado.simulator;

import java.time.Duration;
import java.time.ZonedDateTime;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.marceloleite.mercado.commons.TimeDivisionController;
import org.marceloleite.mercado.dao.mixed.TradeDatabaseSiteDAO;
import org.marceloleite.mercado.simulator.property.SimulatorPropertiesRetriever;
import org.springframework.stereotype.Component;

@Component
public class SimulatorConfigurator {

	@Inject
	private SimulatorPropertiesRetriever simulatorPropertiesRetriever;

	@PostConstruct
	public void postConstruct() {
//		TradeSiteRetriever.setConfiguredStepDuration(simulatorPropertiesRetriever.retrieveTradeSiteDurationStep());
		TradeDatabaseSiteDAO.setIgnoreValuesOnDatabase(simulatorPropertiesRetriever.retrieveIgnoreTradesOnDatabase());
	}

	public Duration getStepDuration() {
		return simulatorPropertiesRetriever.retrieveStepDuration();
	}

	public TimeDivisionController getTimeDivisionController() {
		ZonedDateTime startTime = simulatorPropertiesRetriever.retrieveStartTime();
		ZonedDateTime endTime = simulatorPropertiesRetriever.retrieveEndTime();
		Duration retrievingDuration = simulatorPropertiesRetriever.retrieveRetrievingDuration();
		return new TimeDivisionController(startTime, endTime, retrievingDuration);
	}

}
