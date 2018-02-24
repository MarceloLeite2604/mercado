package org.marceloleite.mercado.simulator.data;

import java.util.HashMap;
import java.util.Map;

public class StrategyData {

	private Map<String, String> parameters;
	
	private Map<String, String> variables;
	
	public StrategyData() {
		this.parameters = new HashMap<>();
		this.variables = new HashMap<>();
	}

	public Map<String, String> getParameters() {
		return parameters;
	}
	
	public Map<String, String> getVariables() {
		return variables;
	}
}
