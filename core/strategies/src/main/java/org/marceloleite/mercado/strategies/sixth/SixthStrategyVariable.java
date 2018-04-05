package org.marceloleite.mercado.strategies.sixth;

import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.strategies.StrategyPropertyUtils;

public enum SixthStrategyVariable implements Property {
	
	WORKING_AMOUNT_CURRENCY("workingAmountCurrency"),
	BASE_TEMPORAL_TICKER("baseTemporalTicker"),
	STATUS("status"),
	GENERATE_DAILY_GRAPHIC("generateDailyGraphic");

	private String name;

	private String value;

	private SixthStrategyVariable(String name) {
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
	
	public static SixthStrategyVariable findByName(String name) {
		return (SixthStrategyVariable)StrategyPropertyUtils.findByName(SixthStrategyVariable.class, name);
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
