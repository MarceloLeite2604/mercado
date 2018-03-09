package org.marceloleite.mercado.tickergenerator.property;

import org.marceloleite.mercado.commons.properties.Property;

public enum TickerGeneratorProperty implements Property {
	
	START_TIME("tickerGenerator.startTime", true),
	END_TIME("tickerGenerator.endTime", false),
	STEP_DURATION("tickerGenerator.stepDuration", true),
	IGNORE_TEMPORAL_TICKERS_ON_DATABASE("tickerGenerator.ignoreTemporalTickersOnDatabase", false);
	
	private String name;
	
	private String value;
	
	private boolean required;
	

	private TickerGeneratorProperty(String name, boolean required) {
		this.name = name;
		this.required = required;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		throw new IllegalAccessError();
		
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

}
