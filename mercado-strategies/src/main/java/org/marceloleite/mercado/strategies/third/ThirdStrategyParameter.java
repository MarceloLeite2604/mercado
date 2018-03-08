package org.marceloleite.mercado.strategies.third;

import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.strategies.StrategyPropertyUtils;

public enum ThirdStrategyParameter implements Property {
	
	GROWTH_PERCENTAGE_THRESHOLD("growthPercentageThreshold", true),
	SHRINK_PERCENTAGE_THRESHOLD("shrinkPercentageThreshold", true);

	private String name;

	private String value;

	private boolean required;

	private ThirdStrategyParameter(String name, boolean required) {
		this.name = name;
		this.required = required;
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
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}
	
	public static ThirdStrategyParameter findByName(String name) {
		return (ThirdStrategyParameter)StrategyPropertyUtils.findByName(ThirdStrategyParameter.class, name);
	}

}
