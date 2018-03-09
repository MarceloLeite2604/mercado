package org.marceloleite.mercado.converter.json.api.negotiation;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.commons.converter.LongToZonedDateTimeConverter;
import org.marceloleite.mercado.jsonmodel.api.data.JsonOperation;
import org.marceloleite.mercado.negotiationapi.model.listorders.Operation;

public class JsonOperationToOperationConverter implements Converter<JsonOperation, Operation> {

	@Override
	public Operation convertTo(JsonOperation jsonOperation) {
		Operation operation = new Operation();
		operation.setId(jsonOperation.getOperationId());
		operation.setQuantity(Double.parseDouble(jsonOperation.getQuantity()));
		operation.setPrice(Double.parseDouble(jsonOperation.getPrice()));
		operation.setFeeRate(Double.parseDouble(jsonOperation.getFeeRate()));
		long longExecutedTimestamp = Long.parseLong(jsonOperation.getExecutedTimestamp());
		operation.setExecutedTimestamp(new LongToZonedDateTimeConverter().convertTo(longExecutedTimestamp));
		return operation;
	}

	@Override
	public JsonOperation convertFrom(Operation operation) {
		throw new UnsupportedOperationException();
	}

	
}
