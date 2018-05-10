package org.marceloleite.mercado.strategies.fourth;

import java.util.Map;

import org.marceloleite.mercado.ObjectDefinitionUtils;
import org.marceloleite.mercado.strategy.ObjectDefinition;

public enum FourthStrategyVariable implements ObjectDefinition {
	
	BASE_TEMPORAL_TICKER("baseTemporalTicker"),
	STATUS("status"),
	CIRCULAR_ARRAY("CircularArray");

	private String name;
	
	private Class<?> objectClass;

	private FourthStrategyVariable(String name) {
		this.name = name;
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
	public boolean isRequired() {
		return true;
	}
	
	public static FourthStrategyVariable findByName(String name) {
		return (FourthStrategyVariable)ObjectDefinitionUtils.getInstance().findByName(FourthStrategyVariable.class, name);
	}

	@Override
	public String getDefaultValue() {
		return null;
	}

	public static Map<String, ObjectDefinition> getObjectDefinitions() {
		return ObjectDefinitionUtils.getInstance()
				.elaborateObjectDefinitionsMap(values());
	}
}
