package org.marceloleite.mercado.converter.entity;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.base.model.data.ClassData;
import org.marceloleite.mercado.base.model.data.ParameterData;
import org.marceloleite.mercado.base.model.data.VariableData;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.ClassIdPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.ClassPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.ParameterPO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.VariablePO;

public class ClassPOToClassdataConverter implements Converter<ClassPO, ClassData>{

	@Override
	public ClassData convertTo(ClassPO classPO) {
		ClassData classData = new ClassData();
		classData.setClassName(classPO.getId().getName());
		List<ParameterData> strategyParameterDatas = createParameterDatas(classPO);
		classData.setParameterDatas(strategyParameterDatas);
		List<VariableData> strategyVariableDatas = createVariableDatas(classPO);
		classData.setVariableDatas(strategyVariableDatas);
		return null;
	}

	@Override
	public ClassPO convertFrom(ClassData classData) {
		ClassPO classPO = new ClassPO();
		ClassIdPO classIdPO = new ClassIdPO();
		classIdPO.setName(classData.getClassName());
		classPO.setId(classIdPO);
		classPO.setParameterPOs(createParameterPOList(classData));
		classPO.setVariablePOs(createVariablePOList(classData));

		
		return null;
	}
	
	private List<VariableData> createVariableDatas(ClassPO classPO) {
		List<VariablePO> variablePOs = classPO.getVariablePOs();

		List<VariableData> variableDatas = new ArrayList<>();

		if (variablePOs != null && !variablePOs.isEmpty()) {
			for (VariablePO variablePO : variablePOs) {
				VariableData variableData = new VariableData();
				variableData.setValue(variablePO.getValue());
				variableDatas.add(variableData);
			}
		}
		return variableDatas;
	}

	private List<ParameterData> createParameterDatas(ClassPO classPO) {
		List<ParameterPO> parameterPOs = classPO.getParameterPOs();

		List<ParameterData> parameterDatas = new ArrayList<>();

		if (parameterPOs != null && !parameterPOs.isEmpty()) {
			for (ParameterPO parameterPO : parameterPOs) {
				ParameterData parameterData = new ParameterData();
				parameterData.setValue(parameterPO.getValue());
				parameterDatas.add(parameterData);
			}
		}
		return parameterDatas;
	}
	
	private List<VariablePO> createVariablePOList(ClassData classData) {
		List<VariableData> variableDatas = classData.getVariableDatas();
		VariablePOToVariableDataConverter variablePOToVariableDataConverter = new VariablePOToVariableDataConverter();
		List<VariablePO> variablePOs = new ArrayList<>();
		if (variableDatas != null && !variableDatas.isEmpty()) {
			for (VariableData variableData : variableDatas) {
				VariablePO variablePO = variablePOToVariableDataConverter.convertFrom(variableData);
				variablePOs.add(variablePO);
			}
		}
		return variablePOs;
	}
	
	private List<ParameterPO> createParameterPOList(ClassData classData) {
		List<ParameterData> parameterDatas = classData.getParameterDatas();
		ParameterPOToParameterDataConverter parameterPOToParameterDataConverter = new ParameterPOToParameterDataConverter();
		List<ParameterPO> parameterPOs = new ArrayList<>();
		if (parameterDatas != null && !parameterDatas.isEmpty()) {
			for (ParameterData parameterData : parameterDatas) {
				ParameterPO parameterPO = parameterPOToParameterDataConverter
						.convertFrom(parameterData);
				parameterPOs.add(parameterPO);
			}
		}
		return parameterPOs;
	}

}
