package org.marceloleite.mercado.strategies.third;

import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.strategies.StrategyPropertyUtils;

public enum ThirdStrategyParameter implements Property {
	
	GROWTH_PERCENTAGE_THRESHOLD("growthPercentageThreshold"),
	SHRINK_PERCENTAGE_THRESHOLD("shrinkPercentageThreshold");

	private String name;

	private String value;

	private ThirdStrategyParameter(String name) {
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

	public static ThirdStrategyParameter findByName(String name) {
		return (ThirdStrategyParameter)StrategyPropertyUtils.findByName(ThirdStrategyParameter.class, name);
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
