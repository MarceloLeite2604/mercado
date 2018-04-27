package org.marceloleite.mercado.strategies.fifth;

import java.util.Map;

import org.marceloleite.mercado.ObjectDefinitionUtils;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.strategy.ObjectDefinition;

public enum FifthStrategyVariable implements ObjectDefinition {

	WORKING_AMOUNT_CURRENCY("workingAmountCurrency", Double.class),
	BASE_TEMPORAL_TICKER("baseTemporalTicker", TemporalTicker.class),
	STATUS("status", FifthStrategyStatus.class);

	private String name;

	private Class<?> objectClass;

	private FifthStrategyVariable(String name, Class<?> objectClass) {
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

	public boolean isRequired() {
		return true;
	}

	public static FifthStrategyVariable findByName(String name) {
		return (FifthStrategyVariable) ObjectDefinitionUtils.getInstance()
				.findByName(FifthStrategyVariable.class, name);
	}

	public static Map<String, ObjectDefinition> getObjectDefinitions() {
		return ObjectDefinitionUtils.getInstance()
				.elaborateObjectDefinitionsMap(values());
	}
}
