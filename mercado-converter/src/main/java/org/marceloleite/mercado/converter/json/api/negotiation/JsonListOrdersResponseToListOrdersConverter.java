package org.marceloleite.mercado.converter.json.api.negotiation;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.jsonmodel.api.data.JsonOrder;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonListOrdersResponse;
import org.marceloleite.mercado.negotiationapi.model.listorders.Order;

public class JsonListOrdersResponseToListOrdersConverter implements Converter<JsonListOrdersResponse, List<Order>> {

	@Override
	public List<Order> convertTo(JsonListOrdersResponse jsonListOrdersResponse) {
		List<Order> orders = new ArrayList<>();
		List<JsonOrder> jsonOrders = jsonListOrdersResponse.getOrders();
		if (jsonOrders != null && !jsonOrders.isEmpty()) {
			JsonOrderToOrderConverter jsonOrderToOrderConverter = new JsonOrderToOrderConverter();
			for (JsonOrder jsonOrder : jsonOrders) {
				orders.add(jsonOrderToOrderConverter.convertTo(jsonOrder));
			}
		}
		return orders;
	}

	@Override
	public JsonListOrdersResponse convertFrom(List<Order> object) {
		throw new UnsupportedOperationException();
	}

}
