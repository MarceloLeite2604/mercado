package org.marceloleite.mercado.strategies.fifth;

import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.strategies.StrategyPropertyUtils;

public enum FifthStrategyVariable implements Property {
	
	WORKING_AMOUNT_CURRENCY("workingAmountCurrency"),
	BASE_TEMPORAL_TICKER("baseTemporalTicker"),
	STATUS("status");

	private String name;

	private String value;

	private FifthStrategyVariable(String name) {
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

	public boolean isRequired() {
		return true;
	}
	
	public static FifthStrategyVariable findByName(String name) {
		return (FifthStrategyVariable)StrategyPropertyUtils.findByName(FifthStrategyVariable.class, name);
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
