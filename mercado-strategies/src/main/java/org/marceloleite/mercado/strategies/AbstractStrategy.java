package org.marceloleite.mercado.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.marceloleite.mercado.base.model.Account;
import org.marceloleite.mercado.base.model.House;
import org.marceloleite.mercado.base.model.Strategy;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.properties.Property;
import org.marceloleite.mercado.converter.data.ListPropertyToListParameterDataConverter;
import org.marceloleite.mercado.converter.data.ListPropertyToListVariableDataConverter;
import org.marceloleite.mercado.data.ClassData;
import org.marceloleite.mercado.data.ParameterData;
import org.marceloleite.mercado.data.VariableData;

public abstract class AbstractStrategy implements Strategy {
	
	private List<Property> parameters;

	private List<Property> variables;

	Class<? extends Enum<? extends Property>> parametersEnumClass;

	Class<? extends Enum<? extends Property>> variablesEnumClass;

	public AbstractStrategy() {
		parametersEnumClass = null;
		variablesEnumClass = null;
	}

	public AbstractStrategy(Class<? extends Enum<? extends Property>> parametersEnumClass,
			Class<? extends Enum<? extends Property>> variablesEnumClass) {
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
		return parameters;
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
			List<String> variableNames = variables.stream().map(Property::getName).collect(Collectors.toList());
			for (Enum<?> e : enumVariables) {
				String parameterName = ((Property) e).getName();
				boolean required = ((Property) e).isRequired();
				if (!variableNames.contains(parameterName) && required) {
					throw new RuntimeException("Variable \"" + parameterName + "\" was not informed for \""
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
					throw new RuntimeException("Parameter \"" + parameterName + "\" was not informed for \""
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
		listVariablesOnLog(variables);
		this.variables = variables;
		for (Property variable : variables) {
			defineVariable(variable);
		}
	}

	public void setParameters(List<Property> parameters) {
		checkParameters(parameters);
		this.parameters = parameters;
		
		for (Property parameter : parameters) {
			defineParameter(parameter);
		}
		listParametersOnLog(parameters);
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

	private void listVariablesOnLog(List<Property> variables) {
		Logger logger = LogManager.getLogger(this.getClass());
		logger.info("Variables: ");
		for (Property variable : variables) {
			logger.info("\t" + variable.getName() + " = " + variable.getValue());
		}
	}

	private void listParametersOnLog(List<Property> parameters) {
		Logger logger = LogManager.getLogger(this.getClass());
		logger.info("Parameters: ");
		for (Property parameter : parameters) {
			logger.info("\t" + parameter.getName() + " = " + parameter.getValue());
		}
	}

	@Override
	public ClassData retrieveData() {
		ClassData classData = new ClassData();
		classData.setName(this.getClass().getName());
		List<Property> variables = retrieveVariables();

		List<VariableData> variableDatas = new ListPropertyToListVariableDataConverter().convertTo(variables);
		variableDatas.forEach(variableData -> variableData.setClassData(classData));
		classData.setVariableDatas(variableDatas);
		
		List<ParameterData> parameterDatas = new ListPropertyToListParameterDataConverter().convertTo(parameters);
		parameterDatas.forEach(parameterData -> parameterData.setClassData(classData));
		classData.setParameterDatas(parameterDatas);
		
		return classData;
	}

	protected abstract Property retrieveVariable(String name);

	protected abstract void defineVariable(Property variable);

	protected abstract void defineParameter(Property parameter);
}
