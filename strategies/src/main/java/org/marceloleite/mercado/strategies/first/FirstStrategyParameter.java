package org.marceloleite.mercado.strategies.first;

import java.util.Map;

import org.marceloleite.mercado.ObjectDefinitionUtils;
import org.marceloleite.mercado.strategy.ObjectDefinition;

public enum FirstStrategyParameter implements ObjectDefinition {

	GROWTH_PERCENTAGE_THRESHOLD("growthPercentageThreshold", Double.class),
	SHRINK_PERCENTAGE_THRESHOLD("shrinkPercentageThreshold", Double.class),
	BUY_STEP_FACTORIAL_NUMBER("buyStepFactorialNumber", Long.class),
	SELL_STEP_FACTORIAL_NUMBER("sellStepFactorialNumber", Long.class);

	private String name;

	private Class<?> objectClass;

	private String defaultValue;

	private FirstStrategyParameter(String name, Class<?> objectClass, String defaultValue) {
		this.name = name;
		this.objectClass = objectClass;
		this.defaultValue = defaultValue;
	}

	private FirstStrategyParameter(String name, Class<?> objectClass) {
		this(name, objectClass, null);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<?> getObjectClass() {
		return objectClass;
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public boolean isRequired() {
		return (defaultValue == null);
	}

	public static FirstStrategyParameter findByName(String name) {
		return (FirstStrategyParameter) ObjectDefinitionUtils.getInstance().findByName(FirstStrategyParameter.class, name);
	}

	public static Map<String, ObjectDefinition> getObjectDefinitions() {
		return ObjectDefinitionUtils.getInstance()
				.elaborateObjectDefinitionsMap(values());
	}
}
