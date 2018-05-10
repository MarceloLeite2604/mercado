package org.marceloleite.mercado.strategies.sixth;

import java.util.Map;

import org.marceloleite.mercado.ObjectDefinitionUtils;
import org.marceloleite.mercado.model.TemporalTicker;
import org.marceloleite.mercado.strategy.ObjectDefinition;

public enum SixthStrategyVariable implements ObjectDefinition {

	WORKING_AMOUNT_CURRENCY("workingAmountCurrency", Double.class),
	BASE_TEMPORAL_TICKER("baseTemporalTicker", TemporalTicker.class),
	STATUS("status", SixthStrategyStatus.class),
	GENERATE_DAILY_GRAPHIC("generateDailyGraphic", Boolean.class);

	private String name;
	private Class<?> objectClass;
	private String defaultValue;

	private SixthStrategyVariable(String name, Class<?> objectClass) {
		this(name, objectClass, null);
	}

	private SixthStrategyVariable(String name, Class<?> objectClass, String defaultValue) {
		this.name = name;
		this.objectClass = objectClass;
		this.defaultValue = defaultValue;
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
		return true;
	}

	public static SixthStrategyVariable findByName(String name) {
		return (SixthStrategyVariable) ObjectDefinitionUtils.getInstance()
				.findByName(SixthStrategyVariable.class, name);
	}
	
	public static Map<String, ObjectDefinition> getObjectDefinitions() {
		return ObjectDefinitionUtils.getInstance()
				.elaborateObjectDefinitionsMap(values());
	}
}
