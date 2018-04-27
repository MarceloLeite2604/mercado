package org.marceloleite.mercado.strategies.first;

import java.util.Map;

import org.marceloleite.mercado.ObjectDefinitionUtils;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.strategy.ObjectDefinition;

public enum FirstStrategyVariable implements ObjectDefinition {

	BUY_SELL_STEP("buySellStep", Long.class),
	BASE_TEMPORAL_TICKER("baseTemporalTicker", TemporalTicker.class);

	private String name;

	private Class<?> objectClass;

	private String defaultValue;

	private FirstStrategyVariable(String name, Class<?> objectClass, String defaultValue) {
		this.name = name;
		this.objectClass = objectClass;
		this.defaultValue = defaultValue;
	}

	private FirstStrategyVariable(String name, Class<?> objectClass) {
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

	public static FirstStrategyVariable findByName(String name) {
		return (FirstStrategyVariable) ObjectDefinitionUtils.getInstance().findByName(FirstStrategyVariable.class, name);
	}

	public static Map<String, ObjectDefinition> getObjectDefinitions() {
		return ObjectDefinitionUtils.getInstance()
				.elaborateObjectDefinitionsMap(values());
	}
}
