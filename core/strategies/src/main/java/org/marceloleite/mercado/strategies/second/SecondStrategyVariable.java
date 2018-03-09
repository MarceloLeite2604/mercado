package org.marceloleite.mercado.strategies.second;

import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.strategies.StrategyPropertyUtils;

public enum SecondStrategyVariable implements Property {
	
	TEMPORAL_TICKER_VARIATION_CIRCULAR_ARRAY("temporalTickerVariationCircularArray"),
	TEMPORAL_TICKER_CIRCULAR_ARRAY("temporalTickerCircularArray"),
	BOSO_CIRCULAR_ARRAY("bosoCircularArray"),
	LAST_FIRST_RATIO_CIRCULAR_ARRAY("lastFirstRatioCircularArray");

	private String name;

	private String value;

	private SecondStrategyVariable(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		throw new UnsupportedOperationException();

	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean isRequired() {
		return true;
	}
	
	public static SecondStrategyVariable findByName(String name) {
		return (SecondStrategyVariable)StrategyPropertyUtils.findByName(SecondStrategyVariable.class, name);
	}
}
