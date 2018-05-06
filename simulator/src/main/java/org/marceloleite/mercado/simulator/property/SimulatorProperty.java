package org.marceloleite.mercado.simulator.property;

import org.marceloleite.mercado.commons.properties.PropertyDefinition;

public enum SimulatorProperty implements PropertyDefinition {

	START_TIME("simulator.startTime"),
	END_TIME("simulator.endTime"),
	STEP_DURATION("simulator.stepDuration"),
	RETRIEVING_DURATION("simulator.retrievingDuration"),
	TRADE_PERCENTAGE("simulator.tradePercentage"),
	THREAD_POOL_SIZE("simulator.threadPoolSize"),
	DURATION_STEP("simulator.durationStep");
	
	private String name;
	
	private SimulatorProperty(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean isRequired() {
		return true;
	}

	@Override
	public String getDefaultValue() {
		return null;
	}

	@Override
	public boolean isEncrypted() {
		return false;
	}
}
