package org.marceloleite.mercado.converter.json.api.negotiation;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.OperationData;
import org.marceloleite.mercado.jsonmodel.api.data.JsonOperation;

public class ListJsonOperationToListOperationDataConverter
		implements Converter<List<JsonOperation>, List<OperationData>> {

	@Override
	public List<OperationData> convertTo(List<JsonOperation> jsonOperations) {
		List<OperationData> operationDatas = new ArrayList<>();
		if (jsonOperations != null && !jsonOperations.isEmpty()) {
			JsonOperationToOperationDataConverter jsonOperationToOperationDataConverter = new JsonOperationToOperationDataConverter();
			for (JsonOperation jsonOperation : jsonOperations) {
				operationDatas.add(jsonOperationToOperationDataConverter.convertTo(jsonOperation));
			}
		}
		return operationDatas;
	}

	@Override
	public List<JsonOperation> convertFrom(List<OperationData> operationDatas) {
		throw new UnsupportedOperationException();
	}

}
