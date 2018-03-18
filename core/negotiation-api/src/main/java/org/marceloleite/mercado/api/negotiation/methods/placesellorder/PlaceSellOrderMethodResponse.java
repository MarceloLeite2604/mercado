package org.marceloleite.mercado.api.negotiation.methods.placesellorder;

import org.marceloleite.mercado.api.negotiation.converters.placesellorder.JsonPlaceSellOrderResponseToPlaceSellOrderResponseConverter;
import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonPlaceSellOrderResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;

public class PlaceSellOrderMethodResponse extends AbstractTapiResponse<JsonPlaceSellOrderResponse, PlaceSellOrderResponse>{
	
	public PlaceSellOrderMethodResponse(JsonTapiResponse jsonTapiResponse) {
		super(jsonTapiResponse, JsonPlaceSellOrderResponse.class, new JsonPlaceSellOrderResponseToPlaceSellOrderResponseConverter());
	}
}
