package org.marceloleite.mercado.strategy;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.model.Parameter;
import org.marceloleite.mercado.model.Strategy;
import org.marceloleite.mercado.model.Variable;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public abstract class AbstractStrategyExecutor implements StrategyExecutor {

	private Strategy strategy;

	protected AbstractStrategyExecutor(Strategy strategy) {
		super();
		this.strategy = strategy;
		setParameters(strategy);
		setVariables(strategy);
	}

	protected Currency getCurrency() {
		return strategy.getCurrency();
	}

	public void setParameters(Strategy strategy) {
		checkParameters(strategy);
		defineParameters(strategy);
		listParametersOnLog(strategy);
	}

	private void checkParameters(Strategy strategy) {
		Map<String, ObjectDefinition> parameterDefinitions = getParameterDefinitions();
		if (!CollectionUtils.isEmpty(parameterDefinitions)) {
			List<String> parameterNames = retrieveInformedParameterNames(strategy);
			for (String parameterDefinitionName : parameterDefinitions.keySet()) {
				if (!parameterNames.contains(parameterDefinitionName)) {
					throw new IllegalStateException("Could not find parameter \"" + parameterDefinitionName
							+ "\" on parameters list for strategy \"" + strategy + "\".");
				}
			}
		} else {
			getLogger().info("No parameters defined for strategy \"" + strategy + "\".");
		}
	}

	private List<String> retrieveInformedParameterNames(Strategy strategy) {
		return Optional.of(strategy.getParameters())
				.orElseThrow(() -> new IllegalStateException(
						"Strategy \"" + strategy + " does not have any parameter defined."))
				.stream()
				.map(Parameter::getName)
				.collect(Collectors.toList());
	}

	private void defineParameters(Strategy strategy) {
		Map<String, ObjectDefinition> parameterDefinitions = getParameterDefinitions();
		Map<String, Parameter> parameterValues = getParameterValues(strategy);
		for (Entry<String, ObjectDefinition> parameterDefinitionEntry : parameterDefinitions.entrySet()) {
			String parameterName = parameterDefinitionEntry.getKey();
			String json = parameterValues.get(parameterName)
					.getValue();
			ObjectDefinition parameterDefinition = parameterDefinitionEntry.getValue();
			Object parameterObject = ObjectToJsonConverter.convertToObject(json, parameterDefinition);
			setParameter(parameterDefinition.getName(), parameterObject);
		}
	}

	private Map<String, Parameter> getParameterValues(Strategy strategy) {
		Map<String, Parameter> parameterValues = strategy.getParameters()
				.stream()
				.collect(Collectors.toMap(Parameter::getName, parameter -> parameter));
		return parameterValues;
	}

	private void listParametersOnLog(Strategy strategy) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Parameters: ");
		for (Parameter parameter : strategy.getParameters()) {
			stringBuilder.append("\t" + parameter.getName() + " = " + parameter.getValue());
		}
		getLogger().info(stringBuilder.toString());
	}

	private List<String> retrieveVariableNames(Strategy strategy) {
		return Optional.of(strategy.getVariables())
				.orElseThrow(() -> new IllegalStateException(
						"Strategy \"" + strategy + " does not have any variable defined."))
				.stream()
				.map(Variable::getName)
				.collect(Collectors.toList());
	}

	private void setVariables(Strategy strategy) {
		checkVariables(strategy);
		defineVariables(strategy);
		listVariablesOnLog(strategy);
	}

	private void checkVariables(Strategy strategy) {
		Map<String, ObjectDefinition> variableDefinitions = getVariableDefinitions();
		if (!CollectionUtils.isEmpty(variableDefinitions)) {
			List<String> variableNames = retrieveVariableNames(strategy);
			for (String variableDefinitionName : variableDefinitions.keySet()) {
				if (!variableNames.contains(variableDefinitionName)) {
					throw new IllegalStateException("Could not find variable \"" + variableDefinitionName
							+ "\" on parameters list for strategy \"" + strategy + "\".");
				}
			}
		} else {
			getLogger().info("No variables defined for strategy \"" + strategy + "\".");
		}
	}

	private void defineVariables(Strategy strategy) {
		Map<String, ObjectDefinition> variableDefinitions = getVariableDefinitions();
		Map<String, Variable> variableValues = getVariableValues(strategy);
		for (Entry<String, ObjectDefinition> variableDefinition : variableDefinitions.entrySet()) {
			String value = variableValues.get(variableDefinition.getKey())
					.getValue();
			ObjectDefinition objectDefinition = variableDefinition.getValue();
			Object variableObject = ObjectToJsonConverter.convertToObject(value, objectDefinition.getClass());
			setVariable(objectDefinition.getName(), variableObject);
		}
	}

	private Map<String, Variable> getVariableValues(Strategy strategy) {
		Map<String, Variable> parameterValues = strategy.getVariables()
				.stream()
				.collect(Collectors.toMap(Variable::getName, variable -> variable));
		return parameterValues;
	}

	private void listVariablesOnLog(Strategy strategy) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Variables: ");
		for (Variable variable : strategy.getVariables()) {
			stringBuilder.append("\t" + variable.getName() + " = " + variable.getValue());
		}
		getLogger().info(stringBuilder.toString());
	}

	@Override
	public Strategy getStrategy() {
		List<Variable> variables = getVariables(strategy);
		strategy.setVariables(variables);
		return strategy;
	}

	private List<Variable> getVariables(Strategy strategy) {
		ObjectWriter writerWithDefaultPrettyPrinter = new ObjectMapper().writerWithDefaultPrettyPrinter();
		List<Variable> variables = strategy.getVariables();
		for (Variable variable : variables) {
			Object object = getVariable(variable.getName());
			try {
				String value = writerWithDefaultPrettyPrinter.writeValueAsString(object);
				variable.setValue(value);
			} catch (JsonProcessingException exception) {
				throw new RuntimeException("Error while retrieving Json value from variable \"" + variable.getName()
						+ "\" of class \"" + object.getClass()
								.getName()
						+ "\".", exception);
			}
		}
		return variables;
	}

	private Logger getLogger() {
		return LogManager.getLogger(this.getClass());
	}

	@Override
	public void beforeStart() {
	}

	@Override
	public void afterFinish() {
	}

	protected abstract void setParameter(String name, Object object);

	protected abstract void setVariable(String name, Object object);

	protected abstract Object getVariable(String name);

	protected abstract Map<String, ObjectDefinition> getParameterDefinitions();

	protected abstract Map<String, ObjectDefinition> getVariableDefinitions();
}
