package org.marceloleite.mercado.api.negotiation.converters.placesellorder;

import org.marceloleite.mercado.api.negotiation.methods.placesellorder.PlaceSellOrderResponse;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.converter.json.api.negotiation.JsonOrderToOrderDataConverter;
import org.marceloleite.mercado.jsonmodel.api.data.JsonOrder;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonPlaceSellOrderResponse;

public class JsonPlaceSellOrderResponseToPlaceSellOrderResponseConverter
		implements Converter<JsonPlaceSellOrderResponse, PlaceSellOrderResponse> {

	@Override
	public PlaceSellOrderResponse convertTo(JsonPlaceSellOrderResponse jsonPlaceSellOrderResult) {
		PlaceSellOrderResponse placeSellOrderResponse = new PlaceSellOrderResponse();
		JsonOrder jsonOrder = jsonPlaceSellOrderResult.getOrder();
		placeSellOrderResponse.setOrder(new JsonOrderToOrderDataConverter().convertTo(jsonOrder));
		return placeSellOrderResponse;
	}

	@Override
	public JsonPlaceSellOrderResponse convertFrom(PlaceSellOrderResponse placeSellOrderResult) {
		throw new UnsupportedOperationException();
	}

}