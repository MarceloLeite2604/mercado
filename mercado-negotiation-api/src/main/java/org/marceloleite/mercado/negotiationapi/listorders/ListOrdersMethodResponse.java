package org.marceloleite.mercado.negotiationapi.listorders;

import java.util.List;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.converter.json.JsonListOrdersResponseToListOrdersConverter;
import org.marceloleite.mercado.jsonmodel.JsonListOrdersResponse;
import org.marceloleite.mercado.jsonmodel.JsonTapiResponse;
import org.marceloleite.mercado.negotiationapi.AbstractTapiResponse;
import org.marceloleite.mercado.negotiationapi.model.Order;

public class ListOrdersMethodResponse extends AbstractTapiResponse<JsonListOrdersResponse, List<Order>> {

	private List<Order> orders;

	public ListOrdersMethodResponse(JsonTapiResponse jsonTapiResponse) {
		super(jsonTapiResponse);
		this.orders = getFormattedResponseData();
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	@Override
	protected Class<?> getJsonResponseDataClass() {
		return JsonListOrdersResponse.class;
	}

	@Override
	protected Converter<JsonListOrdersResponse, List<Order>> getConverter() {
		return new JsonListOrdersResponseToListOrdersConverter();
	}	
}
