package org.marceloleite.mercado.data;

import java.util.List;

public class ClassData {

	private StrategyData strategyData;

	private String name;
	
	private List<ParameterData> parameterDatas;
	
	private List<VariableData> variableDatas;

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

	public List<ParameterData> getParameterDatas() {
		return parameterDatas;
	}

	public void setParameterDatas(List<ParameterData> parameterDatas) {
		this.parameterDatas = parameterDatas;
	}

	public List<VariableData> getVariableDatas() {
		return variableDatas;
	}

	public void setVariableDatas(List<VariableData> variableDatas) {
		this.variableDatas = variableDatas;
	}

}
