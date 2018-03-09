package org.marceloleite.mercado.strategies.fourth;

import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.strategies.StrategyPropertyUtils;

public enum FourthStrategyParameter implements Property {
	
	GROWTH_PERCENTAGE_THRESHOLD("growthPercentageThreshold", true),
	SHRINK_PERCENTAGE_THRESHOLD("shrinkPercentageThreshold", true),
	CIRCULAR_ARRAY_SIZE("circularArraySize", true);
	
	private String name;
	
	private String value;
	
	private boolean required;

	private FourthStrategyParameter(String name, boolean required) {
		this.name = name;
		this.required = required;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		throw new UnsupportedOperationException();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean isRequired() {
		return required;
	}
	
	public static FourthStrategyParameter findByName(String name) {
		return (FourthStrategyParameter)StrategyPropertyUtils.findByName(FourthStrategyParameter.class, name);
	}

}
