package org.marceloleite.mercado.simulator.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.properties.Property;
import org.marceloleite.mercado.simulator.Account;
import org.marceloleite.mercado.simulator.House;

public abstract class AbstractStrategy implements Strategy {

	private List<Property> variables;

	Class<Enum<? extends Property>> parametersEnumClass;

	Class<Enum<? extends Property>> variablesEnumClass;

	public AbstractStrategy() {
		parametersEnumClass = null;
		variablesEnumClass = null;
	}

	public AbstractStrategy(Class<Enum<? extends Property>> parametersEnumClass,
			Class<Enum<? extends Property>> variablesEnumClass) {
		super();
		this.parametersEnumClass = parametersEnumClass;
		this.variablesEnumClass = variablesEnumClass;
	}

	@Override
	public void check(TimeInterval simulationTimeInterval, Account account, House house) {
		if (!areParametersDefined()) {
			throw new RuntimeException("Cannot check execution for \"" + this.getClass()
					+ "\" strategy class. The parameters were not defined.");
		}

		if (!areVariablesDefined()) {
			throw new RuntimeException("Cannot check execution for \"" + this.getClass()
					+ "\" strategy class. The variables were not defined.");
		}
	}

	@Override
	public List<Property> getParameters() {
		return variables;
	}

	private boolean areParametersDefined() {
		return (variables != null);
	}

	private boolean areVariablesDefined() {
		return (variables != null);
	}

	private void checkVariables(List<Property> variables) {
		if (variablesEnumClass != null) {
			Enum<?>[] enumVariables = variablesEnumClass.getEnumConstants();
			List<String> parameterNames = variables.stream().map(Property::getName).collect(Collectors.toList());
			for (Enum<?> e : enumVariables) {
				String parameterName = ((Property) e).getName();
				boolean required = ((Property) e).isRequired();
				if (!parameterNames.contains(parameterName) && required) {
					throw new RuntimeException("Parameter \"" + parameterName + "\" was not informed for \""
							+ this.getClass().getName() + "\" class.");
				}
			}
		}
	}

	private void checkParameters(List<Property> parameters) {
		if (parametersEnumClass != null) {
			Enum<?>[] enumConstants = parametersEnumClass.getEnumConstants();
			List<String> parameterNames = parameters.stream().map(Property::getName).collect(Collectors.toList());
			for (Enum<?> e : enumConstants) {
				String parameterName = ((Property) e).getName();
				boolean required = ((Property) e).isRequired();
				if (!parameterNames.contains(parameterName) && required) {
					throw new RuntimeException("Variable \"" + parameterName + "\" was not informed for \""
							+ this.getClass().getName() + "\" class.");
				}
			}
		}
	}

	public List<Property> getVariables() {
		variables = retrieveVariables();
		checkVariables(variables);
		return variables;
	}

	public void setVariables(List<Property> variables) {
		checkVariables(variables);
		this.variables = variables;
		for (Property variable : variables) {
			defineParameter(variable);
		}
	}

	public void setParameters(List<Property> parameters) {
		checkParameters(parameters);
		for (Property parameter : parameters) {
			defineParameter(parameter);
		}
	}

	private List<Property> retrieveVariables() {
		List<Property> properties = new ArrayList<>();
		if (variablesEnumClass != null) {
			Enum<?>[] enumConstants = variablesEnumClass.getEnumConstants();
			for (Enum<?> enumConstant : enumConstants) {
				Property property = (Property) enumConstant;
				properties.add(retrieveVariable(property.getName()));
				
			}
		}
		return properties;
	}
	
	protected abstract Property retrieveVariable(String name);

	protected abstract void defineVariable(Property variable);

	protected abstract void defineParameter(Property parameter);
}