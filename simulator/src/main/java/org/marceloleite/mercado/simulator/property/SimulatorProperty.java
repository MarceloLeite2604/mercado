package org.marceloleite.mercado.simulator.property;

import org.marceloleite.mercado.commons.properties.Property;

public enum SimulatorProperty implements Property {

	HOUSE_TRADE_PERCENTAGE("simulator.house.tradePercentage", "0.07"),
	START_TIME("simulator.startTime"),
	END_TIME("simulator.endTime"),
	STEP_DURATION("simulator.stepDuration", "30"),
	RETRIEVING_DURATION("simulator.retrievingDuration", "600");

	private String name;
	
	private String defaultValue;
	
	private String value;

	private SimulatorProperty(String name, String defaultvalue) {
		this.name = name;
		this.defaultValue = defaultvalue;
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
		return (defaultValue == null);
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
