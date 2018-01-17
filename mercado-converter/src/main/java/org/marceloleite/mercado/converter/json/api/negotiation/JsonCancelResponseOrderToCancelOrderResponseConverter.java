package org.marceloleite.mercado.converter.json.api.negotiation;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonCancelOrderResponse;
import org.marceloleite.mercado.negotiationapi.model.cancelorder.CancelOrderResponse;

public class JsonCancelResponseOrderToCancelOrderResponseConverter implements Converter<JsonCancelOrderResponse, CancelOrderResponse> {

	@Override
	public CancelOrderResponse convertTo(JsonCancelOrderResponse jsonCancelOrderResponse) {
		CancelOrderResponse cancelOrderResponse = new CancelOrderResponse();
		cancelOrderResponse.setOrder(new JsonOrderToOrderConverter().convertTo(jsonCancelOrderResponse.getOrder()));
		return cancelOrderResponse;
	}

	@Override
	public JsonCancelOrderResponse convertFrom(CancelOrderResponse cancelOrderResponse) {
		throw new UnsupportedOperationException();
	}

}
