package org.marceloleite.mercado.api.negotiation.methods.placebuyorder;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiResponse;
import org.marceloleite.mercado.converter.json.api.negotiation.JsonPlaceBuyOrderResponseToPlaceBuyOrderResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonPlaceBuyOrderResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.negotiationapi.model.placebuyorder.PlaceBuyOrderResponse;

public class PlaceBuyOrderMethodResponse
		extends AbstractTapiResponse<JsonPlaceBuyOrderResponse, PlaceBuyOrderResponse> {

	public PlaceBuyOrderMethodResponse(JsonTapiResponse jsonTapiResponse) {
		super(jsonTapiResponse, JsonPlaceBuyOrderResponse.class, new JsonPlaceBuyOrderResponseToPlaceBuyOrderResponse());
	}
}
