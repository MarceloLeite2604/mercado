package org.marceloleite.mercado.api.negotiation.methods.listorderbook;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiResponse;
import org.marceloleite.mercado.converter.json.api.negotiation.listorderbook.JsonListOrderbookResponseToListOrderbookResponseConverter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.orderbook.JsonListOrderbookResponse;
import org.marceloleite.mercado.negotiationapi.model.listorderbook.ListOrderbookResponse;

public class ListOrderbookMethodResponse
		extends AbstractTapiResponse<JsonListOrderbookResponse, ListOrderbookResponse> {

	public ListOrderbookMethodResponse(JsonTapiResponse jsonTapiResponse) {
		super(jsonTapiResponse, JsonListOrderbookResponse.class,
				new JsonListOrderbookResponseToListOrderbookResponseConverter());
	}
}
