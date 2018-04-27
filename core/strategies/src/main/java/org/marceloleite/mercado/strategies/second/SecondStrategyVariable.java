package org.marceloleite.mercado.strategies.second;

import java.util.Map;

import org.marceloleite.mercado.ObjectDefinitionUtils;
import org.marceloleite.mercado.commons.CircularArray;
import org.marceloleite.mercado.strategy.ObjectDefinition;

public enum SecondStrategyVariable implements ObjectDefinition {
	
	TEMPORAL_TICKER_VARIATION_CIRCULAR_ARRAY("temporalTickerVariationCircularArray", CircularArray.class),
	TEMPORAL_TICKER_CIRCULAR_ARRAY("temporalTickerCircularArray", CircularArray.class),
	BOSO_CIRCULAR_ARRAY("bosoCircularArray", CircularArray.class),
	LAST_FIRST_RATIO_CIRCULAR_ARRAY("lastFirstRatioCircularArray", CircularArray.class);
	
	private String name;
	
	private Class<?> objectClass;

	private SecondStrategyVariable(String name, Class<?> objectClass) {
		this.name = name;
		this.objectClass = objectClass;
	}

	public String getName() {
		return name;
	}

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

	public static SecondStrategyVariable findByName(String name) {
		return (SecondStrategyVariable)ObjectDefinitionUtils.getInstance().findByName(SecondStrategyVariable.class, name);
	}

	public static Map<String, ObjectDefinition> getObjectDefinitions() {
		return ObjectDefinitionUtils.getInstance().elaborateObjectDefinitionsMap(values());
	}

	
}
