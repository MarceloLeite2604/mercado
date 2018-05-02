package org.marceloleite.mercado.api.negotiation.converters.placesellorder;

import org.marceloleite.mercado.api.negotiation.methods.placesellorder.PlaceSellOrderResponse;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.converter.json.api.negotiation.JsonOrderToOrderDataConverter;
import org.marceloleite.mercado.jsonmodel.api.data.JsonOrder;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonPlaceSellOrderResponse;

public class JsonPlaceSellOrderResponseToPlaceSellOrderResponseConverter
		implements Converter<PlaceSellOrderResponse, PlaceSellOrderResponse> {

	@Override
	public PlaceSellOrderResponse convertTo(PlaceSellOrderResponse jsonPlaceSellOrderResult) {
		PlaceSellOrderResponse placeSellOrderResponse = new PlaceSellOrderResponse();
		JsonOrder jsonOrder = jsonPlaceSellOrderResult.getOrder();
		placeSellOrderResponse.setOrder(new JsonOrderToOrderDataConverter().convertTo(jsonOrder));
		return placeSellOrderResponse;
	}

	@Override
	public PlaceSellOrderResponse convertFrom(PlaceSellOrderResponse placeSellOrderResult) {
		throw new UnsupportedOperationException();
	}

}
