package org.marceloleite.mercado.api.negotiation.methods.listorders;

import java.util.List;

import org.marceloleite.mercado.api.negotiation.methods.TapiResponseTemplate;
import org.marceloleite.mercado.converter.json.api.negotiation.JsonListOrdersResponseToListOrdersConverter;
import org.marceloleite.mercado.data.OrderData;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonListOrdersResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;

public class ListOrdersMethodResponse extends TapiResponseTemplate<ListOrdersResponse, List<OrderData>> {

	public ListOrdersMethodResponse(NapiResponse jsonTapiResponse) {
		super(jsonTapiResponse, ListOrdersResponse.class, new JsonListOrdersResponseToListOrdersConverter());
	}
}
