package org.marceloleite.mercado.converter.entity;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.OperationData;
import org.marceloleite.mercado.databaseretriever.persistence.objects.OperationPO;

public class ListOperationPOToListOperationDataConverter implements Converter<List<OperationPO>, List<OperationData>> {

	@Override
	public List<OperationData> convertTo(List<OperationPO> operationPOs) {
		List<OperationData> operationDatas = new ArrayList<>();
		if (operationPOs != null && !operationPOs.isEmpty()) {
			OperationPOToOperationDataConverter operationPOToOperationDataConverter = new OperationPOToOperationDataConverter();
			for (OperationPO operationPO : operationPOs) {
				operationDatas.add(operationPOToOperationDataConverter.convertTo(operationPO));
			}
		}
		return operationDatas;
	}

	@Override
	public List<OperationPO> convertFrom(List<OperationData> operationDatas) {
		List<OperationPO> operationPOs = new ArrayList<>();
		if (operationDatas != null && !operationDatas.isEmpty()) {
			OperationPOToOperationDataConverter operationPOToOperationDataConverter = new OperationPOToOperationDataConverter();
			for (OperationData operationData : operationDatas) {
				operationPOs.add(operationPOToOperationDataConverter.convertFrom(operationData));
			}
		}
		return operationPOs;
	}

}
