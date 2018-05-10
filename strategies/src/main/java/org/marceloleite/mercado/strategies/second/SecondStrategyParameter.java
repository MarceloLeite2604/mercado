package org.marceloleite.mercado.strategies.second;

import java.util.Map;

import org.marceloleite.mercado.ObjectDefinitionUtils;
import org.marceloleite.mercado.strategy.ObjectDefinition;

public enum SecondStrategyParameter implements ObjectDefinition {
	
	TOTAL_TIME_INTERVAL_TO_ANALYZE("totalTimeIntervalToAnalyze", Integer.class);

	private String name;
	private Class<?> objectClass;
	private String defaultValue;
	
	private SecondStrategyParameter(String name, Class<?> objectClass) {
		this(name, objectClass, null);
	}

	private SecondStrategyParameter(String name, Class<?> objectClass, String defaultValue) {
		this.name = name;
		this.objectClass = objectClass;
		this.defaultValue = defaultValue;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public Class<?> getObjectClass() {
		return objectClass;
	}

	@Override
	public String getDefaultValue() {
		return null;
	}

	@Override
	public boolean isRequired() {
		return (defaultValue == null);
	}
	
	public static Map<String, ObjectDefinition> getObjectDefinitions() {
		return ObjectDefinitionUtils.getInstance()
				.elaborateObjectDefinitionsMap(values());
	}

	public static SecondStrategyParameter findByName(String name) {
		return (SecondStrategyParameter)ObjectDefinitionUtils.getInstance().findByName(SecondStrategyParameter.class, name);
	}	
}
