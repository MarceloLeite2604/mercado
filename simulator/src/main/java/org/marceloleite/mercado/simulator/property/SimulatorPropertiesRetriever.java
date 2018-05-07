package org.marceloleite.mercado.simulator.property;

import java.time.Duration;
import java.time.ZonedDateTime;

import javax.inject.Inject;

import org.marceloleite.mercado.commons.utils.DurationUtils;
import org.marceloleite.mercado.commons.utils.ZonedDateTimeUtils;
import org.springframework.stereotype.Component;

@Component
public class SimulatorPropertiesRetriever {

	@Inject
	private SimulatorProperties simulatorProperties;

	public ZonedDateTime retrieveStartTime() {
		return ZonedDateTimeUtils.parse(simulatorProperties.getStartTime());
	}

	public ZonedDateTime retrieveEndTime() {
		return ZonedDateTimeUtils.parse(simulatorProperties.getEndTime());
	}

	public Duration retrieveStepDuration() {
		return DurationUtils.parseFromSeconds(simulatorProperties.getStepDuration());
	}

	public Duration retrieveRetrievingDuration() {
		return DurationUtils.parseFromSeconds(simulatorProperties.getRetrievingDuration());
	}
	
	public Integer retrieveThreadPoolSize() {
		return simulatorProperties.getThreadPoolSize();
	}
}
