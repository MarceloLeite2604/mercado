package org.marceloleite.mercado.strategies.fourth;

import java.util.Map;

import org.marceloleite.mercado.ObjectDefinitionUtils;
import org.marceloleite.mercado.strategy.ObjectDefinition;

public enum FourthStrategyParameter implements ObjectDefinition {
	
	GROWTH_PERCENTAGE_THRESHOLD("growthPercentageThreshold", Double.class),
	SHRINK_PERCENTAGE_THRESHOLD("shrinkPercentageThreshold", Double.class),
	CIRCULAR_ARRAY_SIZE("circularArraySize", Integer.class);
	
	private String name;

	private Class<?> objectClass;

	private String defaultValue;

	private FourthStrategyParameter(String name, Class<?> clazz, String defaultValue) {
		this.name = name;
		this.objectClass = clazz;
		this.defaultValue = defaultValue;
	}
	
	private FourthStrategyParameter(String name, Class<?> clazz) {
		this.name = name;
		this.objectClass = clazz;
		this.defaultValue = null;
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
	
	public static Map<String, ObjectDefinition> getObjectDefinitions() {
		return ObjectDefinitionUtils.getInstance()
				.elaborateObjectDefinitionsMap(values());
	}
	
	public static FourthStrategyParameter findByName(String name) {
		return (FourthStrategyParameter) ObjectDefinitionUtils.getInstance().findByName(FourthStrategyParameter.class, name);
	}
}
