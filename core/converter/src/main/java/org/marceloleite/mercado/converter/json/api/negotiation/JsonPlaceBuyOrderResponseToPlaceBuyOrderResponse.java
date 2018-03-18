package org.marceloleite.mercado.converter.json.api.negotiation;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.OrderData;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonPlaceBuyOrderResponse;
import org.marceloleite.mercado.negotiationapi.model.placebuyorder.PlaceBuyOrderResponse;

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
