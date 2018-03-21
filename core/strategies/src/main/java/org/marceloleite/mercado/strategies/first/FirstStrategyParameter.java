package org.marceloleite.mercado.strategies.first;

import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.strategies.StrategyPropertyUtils;

public enum FirstStrategyParameter implements Property {

	GROWTH_PERCENTAGE_THRESHOLD("growthPercentageThreshold"),
	SHRINK_PERCENTAGE_THRESHOLD("shrinkPercentageThreshold"),
	BUY_STEP_FACTORIAL_NUMBER("buyStepFactorialNumber"),
	SELL_STEP_FACTORIAL_NUMBER("sellStepFactorialNumber");

	private String name;

	private String value;

	private FirstStrategyParameter(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		throw new UnsupportedOperationException();

	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean isRequired() {
		return true;
	}
	
	public static FirstStrategyParameter findByName(String name) {
		return (FirstStrategyParameter)StrategyPropertyUtils.findByName(FirstStrategyParameter.class, name);
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
