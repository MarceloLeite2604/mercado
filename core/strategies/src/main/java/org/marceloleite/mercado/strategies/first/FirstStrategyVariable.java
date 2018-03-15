package org.marceloleite.mercado.strategies.first;

import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.strategies.StrategyPropertyUtils;

public enum FirstStrategyVariable implements Property {

	BUY_SELL_STEP("buySellStep"),
	BASE_TEMPORAL_TICKER("baseTemporalTicker");

	private String name;

	private String value;

	private FirstStrategyVariable(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		throw new UnsupportedOperationException();

	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean isRequired() {
		return true;
	}

	public static FirstStrategyVariable findByName(String name) {
		return (FirstStrategyVariable)StrategyPropertyUtils.findByName(FirstStrategyVariable.class, name);
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
