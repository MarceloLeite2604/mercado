package org.marceloleite.mercado.strategies.sixth;

import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.strategies.StrategyPropertyUtils;

public enum SixthStrategyParameter implements Property {
	
	GROWTH_PERCENTAGE_THRESHOLD("growthPercentageThreshold"),
	SHRINK_PERCENTAGE_THRESHOLD("shrinkPercentageThreshold"),
	WORKING_AMOUNT_CURRENCY("workingAmountCurrency"),
	CIRCULAR_ARRAY_SIZE("circularArraySize"),
	INITIAL_STATUS("initialStatus", "saved"),
	NEXT_VALUE_STEPS("nextValueSteps", "1"),
	GENERATE_DAILY_GRAPHIC("generateDailyGraphic", "false");

	private String name;

	private String value;
	
	private String defaultValue;

	private SixthStrategyParameter(String name, String defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}
	
	private SixthStrategyParameter(String name) {
		this(name, null);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isRequired() {
		return (defaultValue == null);
	}

	public static SixthStrategyParameter findByName(String name) {
		return (SixthStrategyParameter)StrategyPropertyUtils.findByName(SixthStrategyParameter.class, name);
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public boolean isEncrypted() {
		return false;
	}

}
