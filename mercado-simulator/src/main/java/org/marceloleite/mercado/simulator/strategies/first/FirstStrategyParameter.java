package org.marceloleite.mercado.simulator.strategies.first;

import org.marceloleite.mercado.properties.Property;
import org.marceloleite.mercado.simulator.strategies.StrategyPropertyUtils;

public enum FirstStrategyParameter implements Property {

	GROWTH_PERCENTAGE_THRESHOLD("growthPercentageThreshold", true),
	SHRINK_PERCENTAGE_THRESHOLD("shrinkPercentageThreshold", true),
	BUY_STEP_FACTORIAL_NUMBER("buyStepFactorialNumber", true),
	SELL_STEP_FACTORIAL_NUMBER("sellStepFactorialNumber", true);

	private String name;

	private String value;

	private boolean required;

	private FirstStrategyParameter(String name, boolean required) {
		this.name = name;
		this.required = required;
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
		return required;
	}
	
	public static FirstStrategyParameter findByName(String name) {
		return (FirstStrategyParameter)StrategyPropertyUtils.findByName(FirstStrategyParameter.class, name);
	}
}
