package org.marceloleite.mercado.api.negotiation.methods.listorders;

import java.util.List;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiResponse;
import org.marceloleite.mercado.converter.json.api.negotiation.JsonListOrdersResponseToListOrdersConverter;
import org.marceloleite.mercado.data.OrderData;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonListOrdersResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;

public class ListOrdersMethodResponse extends AbstractTapiResponse<JsonListOrdersResponse, List<OrderData>> {

	public ListOrdersMethodResponse(JsonTapiResponse jsonTapiResponse) {
		super(jsonTapiResponse, JsonListOrdersResponse.class, new JsonListOrdersResponseToListOrdersConverter());
	}
}
