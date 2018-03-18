package org.marceloleite.mercado.api.negotiation.converters.placebuyorder;

import org.marceloleite.mercado.api.negotiation.methods.placebuyorder.PlaceBuyOrderResponse;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.converter.json.api.negotiation.JsonOrderToOrderDataConverter;
import org.marceloleite.mercado.data.OrderData;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonPlaceBuyOrderResponse;

public class JsonPlaceBuyOrderResponseToPlaceBuyOrderResponse
		implements Converter<JsonPlaceBuyOrderResponse, PlaceBuyOrderResponse> {

	@Override
	public PlaceBuyOrderResponse convertTo(JsonPlaceBuyOrderResponse jsonPlaceBuyOrderResponse) {
		PlaceBuyOrderResponse placeBuyOrderResponse = new PlaceBuyOrderResponse();
		OrderData orderData = new JsonOrderToOrderDataConverter().convertTo(jsonPlaceBuyOrderResponse.getOrder());
		placeBuyOrderResponse.setOrder(orderData);
		return placeBuyOrderResponse;
	}

	@Override
	public JsonPlaceBuyOrderResponse convertFrom(PlaceBuyOrderResponse placeBuyOrderResponse) {
		throw new UnsupportedOperationException();
	}

}
