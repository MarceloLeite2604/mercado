package org.marceloleite.mercado.api.negotiation.methods.placebuyorder;

import org.marceloleite.mercado.api.negotiation.converters.placebuyorder.JsonPlaceBuyOrderResponseToPlaceBuyOrderResponse;
import org.marceloleite.mercado.api.negotiation.methods.TapiResponseTemplate;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonPlaceBuyOrderResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;

public class PlaceBuyOrderMethodResponse
		extends TapiResponseTemplate<PlaceBuyOrderResponse, PlaceBuyOrderResponse> {

	public PlaceBuyOrderMethodResponse(NapiResponse jsonTapiResponse) {
		super(jsonTapiResponse, PlaceBuyOrderResponse.class, new JsonPlaceBuyOrderResponseToPlaceBuyOrderResponse());
	}
}
