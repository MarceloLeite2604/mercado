package org.marceloleite.mercado.simulator.property;

import org.marceloleite.mercado.commons.properties.Property;

public enum SimulatorProperty implements Property {

	HOUSE_TRADE_PERCENTAGE("simulator.house.tradePercentage", true),
	START_TIME("simulator.startTime", true),
	END_TIME("simulator.endTime", true),
	STEP_DURATION("simulator.stepDuration", true),
	RETRIEVING_DURATION("simulator.retrievingDuration", true);

	private String name;
	
	private String value;

	private boolean required;

	private SimulatorProperty(String name, boolean required) {
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
	public void setName(String name) {
		throw new IllegalAccessError();
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
		
	}

	@Override
	public boolean isRequired() {
		return this.required;
	}
}
