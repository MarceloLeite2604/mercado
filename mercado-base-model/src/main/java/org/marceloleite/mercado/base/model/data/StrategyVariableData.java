package org.marceloleite.mercado.base.model.data;

public class StrategyVariableData {

	private StrategyData strategyData;

	private String name;

	private String value;

	public StrategyData getStrategyData() {
		return strategyData;
	}

	public void setStrategyData(StrategyData strategyDataModel) {
		this.strategyData = strategyDataModel;
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
