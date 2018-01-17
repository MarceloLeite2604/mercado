package org.marceloleite.mercado.api.negotiation.methods.placesellorder;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiResponse;
import org.marceloleite.mercado.converter.json.api.negotiation.JsonPlaceSellOrderResultToPlaceSellOrderResultConverter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonPlaceSellOrderResult;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.negotiationapi.model.placesellorder.PlaceSellOrderResult;

public class PlaceSellOrderMethodResult extends AbstractTapiResponse<JsonPlaceSellOrderResult, PlaceSellOrderResult>{
	
	public PlaceSellOrderMethodResult(JsonTapiResponse jsonTapiResponse) {
		super(jsonTapiResponse, JsonPlaceSellOrderResult.class, new JsonPlaceSellOrderResultToPlaceSellOrderResultConverter());
	}
}
