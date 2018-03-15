package org.marceloleite.mercado.strategies.second;

import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.commons.properties.StandardProperty;

public enum SecondStrategyParameter implements Property {
	
	TOTAL_TIME_INTERVAL_TO_ANALYZE("totalTimeIntervalToAnalyze");

	private String name;
	
	private String value;

	private SecondStrategyParameter(String name) {
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
	
	public StandardProperty toStandardProperty() {
		return new StandardProperty(name, value, true);
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
