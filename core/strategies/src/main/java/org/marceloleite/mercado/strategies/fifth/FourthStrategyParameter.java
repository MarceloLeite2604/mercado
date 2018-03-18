package org.marceloleite.mercado.strategies.fifth;

import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.strategies.StrategyPropertyUtils;

public enum FourthStrategyParameter implements Property {
	
	GROWTH_PERCENTAGE_THRESHOLD("growthPercentageThreshold"),
	SHRINK_PERCENTAGE_THRESHOLD("shrinkPercentageThreshold"),
	CIRCULAR_ARRAY_SIZE("circularArraySize");
	
	private String name;
	
	private String value;

	private FourthStrategyParameter(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		throw new UnsupportedOperationException();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean isRequired() {
		return true;
	}
	
	public static FourthStrategyParameter findByName(String name) {
		return (FourthStrategyParameter)StrategyPropertyUtils.findByName(FourthStrategyParameter.class, name);
	}

	@Override
	public String getDefaultValue() {
		return null;
	}

	@Override
	public boolean isEncrypted() {
		return false;
	}

}
