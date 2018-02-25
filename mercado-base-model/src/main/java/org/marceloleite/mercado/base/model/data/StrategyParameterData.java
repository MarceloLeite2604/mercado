package org.marceloleite.mercado.base.model.data;

public class StrategyParameterData {

	private StrategyData strategyData;

	private String name;

	private String value;

	public StrategyData getStrategyData() {
		return strategyData;
	}

	public void setStrategyData(StrategyData strategyData) {
		this.strategyData = strategyData;
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
}
