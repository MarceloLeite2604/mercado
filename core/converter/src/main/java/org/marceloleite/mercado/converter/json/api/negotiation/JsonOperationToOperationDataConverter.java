package org.marceloleite.mercado.converter.json.api.negotiation;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.commons.converter.LongToZonedDateTimeConverter;
import org.marceloleite.mercado.data.OperationData;
import org.marceloleite.mercado.jsonmodel.api.data.JsonOperation;

public class JsonOperationToOperationDataConverter implements Converter<JsonOperation, OperationData> {

	@Override
	public OperationData convertTo(JsonOperation jsonOperation) {
		OperationData operationData = new OperationData();
		operationData.setId(jsonOperation.getOperationId());
		operationData.setQuantity(Double.parseDouble(jsonOperation.getQuantity()));
		operationData.setPrice(Double.parseDouble(jsonOperation.getPrice()));
		operationData.setFeeRate(Double.parseDouble(jsonOperation.getFeeRate()));
		long longExecutedTimestamp = Long.parseLong(jsonOperation.getExecutedTimestamp());
		operationData.setExecuted(LongToZonedDateTimeConverter.getInstance().convertTo(longExecutedTimestamp));
		return operationData;
	}

	@Override
	public JsonOperation convertFrom(OperationData operation) {
		throw new UnsupportedOperationException();
	}

	
}
