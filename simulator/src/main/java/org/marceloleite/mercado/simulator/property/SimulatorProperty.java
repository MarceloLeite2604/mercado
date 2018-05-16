package org.marceloleite.mercado.simulator.property;

import org.marceloleite.mercado.commons.properties.PropertyDefinition;

public enum SimulatorProperty implements PropertyDefinition {

	START_TIME("simulator.startTime"),
	END_TIME("simulator.endTime"),
	STEP_DURATION("simulator.stepDuration"),
	RETRIEVING_DURATION("simulator.retrievingDuration"),
	TRADE_PERCENTAGE("simulator.tradePercentage"),
	THREAD_POOL_SIZE("simulator.threadPoolSize"),
	DURATION_STEP("simulator.durationStep"),
	IGNORE_TRADES_ON_DATABASE("simulator.ignoreTradesOnDatabase", "false");
	
	private String name;
	
	private String defaultValue;
	
	private SimulatorProperty(String name, String defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}
	
	private SimulatorProperty(String name) {
		this(name, null);
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
		return defaultValue;
	}

	@Override
	public boolean isEncrypted() {
		return false;
	}
}
