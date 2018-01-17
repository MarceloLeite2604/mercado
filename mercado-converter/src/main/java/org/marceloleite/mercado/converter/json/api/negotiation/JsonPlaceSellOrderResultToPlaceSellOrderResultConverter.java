package org.marceloleite.mercado.converter.json.api.negotiation;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.jsonmodel.api.data.JsonOrder;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonPlaceSellOrderResult;
import org.marceloleite.mercado.negotiationapi.model.placesellorder.PlaceSellOrderResult;

public class JsonPlaceSellOrderResultToPlaceSellOrderResultConverter
		implements Converter<JsonPlaceSellOrderResult, PlaceSellOrderResult> {

	@Override
	public PlaceSellOrderResult convertTo(JsonPlaceSellOrderResult jsonPlaceSellOrderResult) {
		PlaceSellOrderResult placeSellOrderResult = new PlaceSellOrderResult();
		JsonOrder jsonOrder = jsonPlaceSellOrderResult.getOrder();
		placeSellOrderResult.setOrder(new JsonOrderToOrderConverter().convertTo(jsonOrder));
		return placeSellOrderResult;
	}

	@Override
	public JsonPlaceSellOrderResult convertFrom(PlaceSellOrderResult placeSellOrderResult) {
		throw new UnsupportedOperationException();
	}

}
