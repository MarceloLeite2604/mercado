package org.marceloleite.mercado.api.negotiation.methods.placesellorder;

import org.marceloleite.mercado.api.negotiation.converters.placesellorder.JsonPlaceSellOrderResponseToPlaceSellOrderResponseConverter;
import org.marceloleite.mercado.api.negotiation.methods.TapiResponseTemplate;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonPlaceSellOrderResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;

public class PlaceSellOrderMethodResponse extends TapiResponseTemplate<PlaceSellOrderResponse, PlaceSellOrderResponse>{
	
	public PlaceSellOrderMethodResponse(NapiResponse jsonTapiResponse) {
		super(jsonTapiResponse, PlaceSellOrderResponse.class, new JsonPlaceSellOrderResponseToPlaceSellOrderResponseConverter());
	}
}
