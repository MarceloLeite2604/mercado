package org.marceloleite.mercado.api.negotiation.methods.listorders;

import java.util.List;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiResponse;
import org.marceloleite.mercado.converter.json.api.negotiation.JsonListOrdersResponseToListOrdersConverter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonListOrdersResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.negotiationapi.model.Order;

public class ListOrdersMethodResponse extends AbstractTapiResponse<JsonListOrdersResponse, List<Order>> {

	public ListOrdersMethodResponse(JsonTapiResponse jsonTapiResponse) {
		super(jsonTapiResponse, JsonListOrdersResponse.class, new JsonListOrdersResponseToListOrdersConverter());
	}
}
