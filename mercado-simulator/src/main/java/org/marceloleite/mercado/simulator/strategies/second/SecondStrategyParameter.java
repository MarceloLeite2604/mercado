package org.marceloleite.mercado.simulator.strategies.second;

import org.marceloleite.mercado.properties.Property;
import org.marceloleite.mercado.properties.StandardProperty;

public enum SecondStrategyParameter implements Property {
	
	TOTAL_TIME_INTERVAL_TO_ANALYZE("totalTimeIntervalToAnalyze", true);

	private String name;
	
	private String value;
	
	private boolean required;

	private SecondStrategyParameter(String name, boolean required) {
		this.name = name;
		this.required = required;
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
		return this.required;
	}
	
	public StandardProperty toStandardProperty() {
		return new StandardProperty(name, value, true);
	}
}
