package org.marceloleite.mercado.api.negotiation.methods.cancelorder;

import org.marceloleite.mercado.api.negotiation.converters.JsonCancelResponseOrderToCancelOrderResponseConverter;
import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonCancelOrderResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;

public class CancelOrderMethodResponse extends AbstractTapiResponse<JsonCancelOrderResponse, CancelOrderResponse> {

	public CancelOrderMethodResponse(JsonTapiResponse jsonTapiResponse) {
		super(jsonTapiResponse, JsonCancelOrderResponse.class, new JsonCancelResponseOrderToCancelOrderResponseConverter());
	}
}
