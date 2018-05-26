package org.marceloleite.mercado.controller.properties;

import java.time.Duration;

import javax.inject.Inject;

import org.marceloleite.mercado.commons.utils.DurationUtils;
import org.springframework.stereotype.Component;

@Component
public class ControllerPropertiesRetriever {

	@Inject
	private ControllerProperties controllerProperties;
	
	public Integer retrieveThreadPoolSize() {
		return controllerProperties.getThreadPoolSize();
	}
	
	public Duration retrieveTradeSiteDurationStep() {
		return DurationUtils.parseFromSeconds(controllerProperties.getTradeSiteDurationStep());
	}
	
	public Double retrieveTradeComission() {
		Double result = null;
		if (controllerProperties.getTradeComissionPercentage() != null) {
			result = controllerProperties.getTradeComissionPercentage()/100.0;
		}
		return result;
	}
}