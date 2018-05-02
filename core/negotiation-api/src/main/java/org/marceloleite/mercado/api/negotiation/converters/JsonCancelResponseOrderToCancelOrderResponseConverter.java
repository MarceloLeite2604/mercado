package org.marceloleite.mercado.api.negotiation.converters;

import org.marceloleite.mercado.api.negotiation.methods.cancelorder.CancelOrderResponse;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.converter.json.api.negotiation.JsonOrderToOrderDataConverter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonCancelOrderResponse;

public class JsonCancelResponseOrderToCancelOrderResponseConverter implements Converter<CancelOrderResponse, CancelOrderResponse> {

	@Override
	public CancelOrderResponse convertTo(CancelOrderResponse jsonCancelOrderResponse) {
		CancelOrderResponse cancelOrderResponse = new CancelOrderResponse();
		cancelOrderResponse.setOrderData(new JsonOrderToOrderDataConverter().convertTo(jsonCancelOrderResponse.getOrder()));
		return cancelOrderResponse;
	}

	@Override
	public CancelOrderResponse convertFrom(CancelOrderResponse cancelOrderResponse) {
		throw new UnsupportedOperationException();
	}

}
