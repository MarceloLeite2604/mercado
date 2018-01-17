package org.marceloleite.mercado.converter.json.api.negotiation;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonPlaceBuyOrderResponse;
import org.marceloleite.mercado.negotiationapi.model.Order;
import org.marceloleite.mercado.negotiationapi.model.placebuyorder.PlaceBuyOrderResponse;

public class JsonPlaceBuyOrderResponseToPlaceBuyOrderResponse
		implements Converter<JsonPlaceBuyOrderResponse, PlaceBuyOrderResponse> {

	@Override
	public PlaceBuyOrderResponse convertTo(JsonPlaceBuyOrderResponse jsonPlaceBuyOrderResponse) {
		PlaceBuyOrderResponse placeBuyOrderResponse = new PlaceBuyOrderResponse();
		Order order = new JsonOrderToOrderConverter().convertTo(jsonPlaceBuyOrderResponse.getOrder());
		placeBuyOrderResponse.setOrder(order);
		return placeBuyOrderResponse;
	}

	@Override
	public JsonPlaceBuyOrderResponse convertFrom(PlaceBuyOrderResponse placeBuyOrderResponse) {
		throw new UnsupportedOperationException();
	}

}
