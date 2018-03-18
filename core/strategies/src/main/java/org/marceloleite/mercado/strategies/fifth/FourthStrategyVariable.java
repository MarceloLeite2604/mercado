package org.marceloleite.mercado.strategies.fifth;

import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.strategies.StrategyPropertyUtils;

public enum FourthStrategyVariable implements Property {
	
	BASE_TEMPORAL_TICKER("baseTemporalTicker"),
	STATUS("status"),
	CIRCULAR_ARRAY("CircularArray");

	private String name;

	private String value;

	private FourthStrategyVariable(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isRequired() {
		return true;
	}
	
	public static FourthStrategyVariable findByName(String name) {
		return (FourthStrategyVariable)StrategyPropertyUtils.findByName(FourthStrategyVariable.class, name);
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
