package org.marceloleite.mercado.api.negotiation.methods.getorder;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiResponse;
import org.marceloleite.mercado.converter.json.api.negotiation.JsonGetOrderResponseToGetOrderResponseConverter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonGetOrderResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.negotiationapi.model.getorder.GetOrderResponse;

public class GetOrderMethodResponse extends AbstractTapiResponse<JsonGetOrderResponse, GetOrderResponse> {

	public GetOrderMethodResponse(JsonTapiResponse jsonTapiResponse) {
		super(jsonTapiResponse, JsonGetOrderResponse.class, new JsonGetOrderResponseToGetOrderResponseConverter());
	}
}
