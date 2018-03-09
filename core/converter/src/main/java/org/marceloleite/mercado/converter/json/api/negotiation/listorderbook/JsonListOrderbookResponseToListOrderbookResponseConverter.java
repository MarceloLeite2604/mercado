package org.marceloleite.mercado.converter.json.api.negotiation.listorderbook;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.orderbook.JsonListOrderbookResponse;
import org.marceloleite.mercado.negotiationapi.model.listorderbook.ListOrderbookResponse;
import org.marceloleite.mercado.negotiationapi.model.listorderbook.Orderbook;

public class JsonListOrderbookResponseToListOrderbookResponseConverter
		implements Converter<JsonListOrderbookResponse, ListOrderbookResponse> {

	@Override
	public ListOrderbookResponse convertTo(JsonListOrderbookResponse jsonListOrderbookResponse) {
		ListOrderbookResponse listOrderbookResponse = new ListOrderbookResponse();
		Orderbook orderbook = new JsonOrderbookToOrderbookConverter().convertTo(jsonListOrderbookResponse.getJsonOrderbook());
		listOrderbookResponse.setOrderbook(orderbook);
		return listOrderbookResponse;
	}

	@Override
	public JsonListOrderbookResponse convertFrom(ListOrderbookResponse listOrderbookResponse) {
		throw new UnsupportedOperationException();
	}

}
