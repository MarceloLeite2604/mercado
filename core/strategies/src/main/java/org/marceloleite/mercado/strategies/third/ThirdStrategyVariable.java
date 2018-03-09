package org.marceloleite.mercado.strategies.third;

import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.strategies.StrategyPropertyUtils;

public enum ThirdStrategyVariable implements Property {
	
	BASE_TEMPORAL_TICKER("baseTemporalTicker"),
	STATUS("status");

	private String name;

	private String value;

	private ThirdStrategyVariable(String name) {
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
	
	public static ThirdStrategyVariable findByName(String name) {
		return (ThirdStrategyVariable)StrategyPropertyUtils.findByName(ThirdStrategyVariable.class, name);
	}
}
