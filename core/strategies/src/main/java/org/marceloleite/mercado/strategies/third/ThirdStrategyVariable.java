package org.marceloleite.mercado.strategies.third;

import java.util.Map;

import org.marceloleite.mercado.ObjectDefinitionUtils;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.strategy.ObjectDefinition;

public enum ThirdStrategyVariable implements ObjectDefinition {

	BASE_TEMPORAL_TICKER("baseTemporalTicker", TemporalTicker.class),
	STATUS("status", ThirdStrategyStatus.class);

	private String name;

	private Class<?> objectClass;

	private ThirdStrategyVariable(String name, Class<?> objectClass) {
		this.name = name;
		this.objectClass = objectClass;
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
		return null;
	}

	@Override
	public boolean isRequired() {
		return true;
	}

	public static ThirdStrategyVariable findByName(String name) {
		return (ThirdStrategyVariable) ObjectDefinitionUtils.getInstance()
				.findByName(ThirdStrategyVariable.class, name);
	}

	public static Map<String, ObjectDefinition> getObjectDefinitions() {
		return ObjectDefinitionUtils.getInstance()
				.elaborateObjectDefinitionsMap(values());
	}
}
