package org.marceloleite.mercado.strategies.first;

import org.marceloleite.mercado.strategy.StrategyParameterDefinition;

public enum FirstStrategyParameter implements StrategyParameterDefinition {

	GROWTH_PERCENTAGE_THRESHOLD("growthPercentageThreshold", Double.class, true),
	SHRINK_PERCENTAGE_THRESHOLD("shrinkPercentageThreshold", Double.class, true),
	BUY_STEP_FACTORIAL_NUMBER("buyStepFactorialNumber", Long.class, true),
	SELL_STEP_FACTORIAL_NUMBER("sellStepFactorialNumber", Long.class, true);

	private String name;

	private Class<?> clazz;

	private boolean required;

	private FirstStrategyParameter(String name, Class<?> clazz, boolean required) {
		this.name = name;
		this.clazz = clazz;
		this.required = required;
	}

	public String getName() {
		return name;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public boolean isRequired() {
		return required;
	}

	public static FirstStrategyParameter findByName(String name) {

		if (name == null) {
			throw new IllegalArgumentException("Parameter name is null.");
		}

		for (FirstStrategyParameter firstStrategyParameter : FirstStrategyParameter.values()) {
			if (name.equals(firstStrategyParameter.getName())) {
				return firstStrategyParameter;
			}
		}

		throw new IllegalArgumentException("Could not find a property with name \"" + name + "\".");
	}	
}
