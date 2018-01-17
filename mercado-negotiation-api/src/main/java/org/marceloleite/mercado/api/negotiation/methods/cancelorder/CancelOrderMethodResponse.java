package org.marceloleite.mercado.api.negotiation.methods.cancelorder;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiResponse;
import org.marceloleite.mercado.converter.json.api.negotiation.JsonCancelResponseOrderToCancelOrderResponseConverter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonCancelOrderResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.negotiationapi.model.cancelorder.CancelOrderResponse;

public class CancelOrderMethodResponse extends AbstractTapiResponse<JsonCancelOrderResponse, CancelOrderResponse> {

	public CancelOrderMethodResponse(JsonTapiResponse jsonTapiResponse) {
		super(jsonTapiResponse, JsonCancelOrderResponse.class, new JsonCancelResponseOrderToCancelOrderResponseConverter());
	}
}
