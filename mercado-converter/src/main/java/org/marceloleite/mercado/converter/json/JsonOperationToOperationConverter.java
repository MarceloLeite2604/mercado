package org.marceloleite.mercado.converter.json;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.commons.util.converter.LongToLocalDateTimeConverter;
import org.marceloleite.mercado.jsonmodel.api.data.JsonOperation;
import org.marceloleite.mercado.negotiationapi.model.order.Operation;

public class JsonOperationToOperationConverter implements Converter<JsonOperation, Operation> {

	@Override
	public Operation convertTo(JsonOperation jsonOperation) {
		Operation operation = new Operation();
		operation.setId(jsonOperation.getOperationId());
		operation.setQuantity(Double.parseDouble(jsonOperation.getQuantity()));
		operation.setPrice(Double.parseDouble(jsonOperation.getPrice()));
		operation.setFeeRate(Double.parseDouble(jsonOperation.getFeeRate()));
		long longExecutedTimestamp = Long.parseLong(jsonOperation.getExecutedTimestamp());
		operation.setExecutedTimestamp(new LongToLocalDateTimeConverter().convertTo(longExecutedTimestamp));
		return operation;
	}

	@Override
	public JsonOperation convertFrom(Operation operation) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
