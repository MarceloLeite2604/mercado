package org.marceloleite.mercado.base.model.data;

import java.util.List;

public class ClassData {

	private StrategyData strategyData;

	private String className;
	
	private List<ParameterData> parameterDatas;
	
	private List<VariableData> variableDatas;

	public StrategyData getStrategyData() {
		return strategyData;
	}

	public void setStrategyData(StrategyData strategyData) {
		this.strategyData = strategyData;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
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
