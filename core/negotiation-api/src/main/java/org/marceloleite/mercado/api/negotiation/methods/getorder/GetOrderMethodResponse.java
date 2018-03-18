package org.marceloleite.mercado.api.negotiation.methods.getorder;

import org.marceloleite.mercado.api.negotiation.converters.JsonGetOrderResponseToGetOrderResponseConverter;
import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonGetOrderResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;

public class GetOrderMethodResponse extends AbstractTapiResponse<JsonGetOrderResponse, GetOrderResponse> {

	public GetOrderMethodResponse(JsonTapiResponse jsonTapiResponse) {
		super(jsonTapiResponse, JsonGetOrderResponse.class, new JsonGetOrderResponseToGetOrderResponseConverter());
	}
}
