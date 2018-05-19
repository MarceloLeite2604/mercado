package org.marceloleite.mercado.strategies.sixth.parameter;

import java.time.LocalTime;
import java.util.Map;

import org.marceloleite.mercado.ObjectDefinitionUtils;
import org.marceloleite.mercado.strategies.sixth.SixthStrategyStatus;
import org.marceloleite.mercado.strategy.ObjectDefinition;

public enum SixthStrategyParameter implements ObjectDefinition {
	
	GROWTH_PERCENTAGE_THRESHOLD("growthPercentageThreshold", Double.class),
	SHRINK_PERCENTAGE_THRESHOLD("shrinkPercentageThreshold", Double.class),
	WORKING_AMOUNT_CURRENCY("workingAmountCurrency", Double.class),
	CIRCULAR_ARRAY_SIZE("circularArraySize", Integer.class),
	INITIAL_STATUS("initialStatus", SixthStrategyStatus.class, "undefined"),
	NEXT_VALUE_STEPS("nextValueSteps", Integer.class, "1"),
	CREATE_DAILY_GRAPHIC("createDailyGraphic", Boolean.class, "false"),
	DAILY_GRAPHIC_TIME("dailyGraphicTime", LocalTime.class, "23:59");

	private String name;

	private Class<?> objectClass;
	
	private String defaultValue;
	
	private SixthStrategyParameter(String name, Class<?> objectClass) {
		this(name, objectClass, null);
	}

	private SixthStrategyParameter(String name, Class<?> objectClass, String defaultValue) {
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
		return (defaultValue == null);
	}

	public static SixthStrategyParameter findByName(String name) {
		return (SixthStrategyParameter)ObjectDefinitionUtils.getInstance().findByName(SixthStrategyParameter.class, name);
	}
	
	public static Map<String, ObjectDefinition> getObjectDefinitions() {
		return ObjectDefinitionUtils.getInstance()
				.elaborateObjectDefinitionsMap(values());
	}
}
