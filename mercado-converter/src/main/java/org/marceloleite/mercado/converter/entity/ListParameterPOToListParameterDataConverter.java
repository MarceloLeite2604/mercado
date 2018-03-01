package org.marceloleite.mercado.converter.entity;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.base.model.data.ParameterData;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.ParameterPO;

public class ListParameterPOToListParameterDataConverter implements Converter<List<ParameterPO>, List<ParameterData>> {

	@Override
	public List<ParameterData> convertTo(List<ParameterPO> parameterPOs) {
		ParameterPOToParameterDataConverter parameterPOToParameterDataConverter = new ParameterPOToParameterDataConverter();
		List<ParameterData> parameterDatas = new ArrayList<>();
		for (ParameterPO parameterPO : parameterPOs) {
			parameterDatas.add(parameterPOToParameterDataConverter.convertTo(parameterPO));
		}
		return parameterDatas;
	}

	@Override
	public List<ParameterPO> convertFrom(List<ParameterData> parameterDatas) {
		ParameterPOToParameterDataConverter parameterPOToParameterDataConverter = new ParameterPOToParameterDataConverter();
		List<ParameterPO> parameterPOs = new ArrayList<>();
		for (ParameterData parameterData : parameterDatas) {
			parameterPOs.add(parameterPOToParameterDataConverter.convertFrom(parameterData));
		}
		return parameterPOs;
	}

}
