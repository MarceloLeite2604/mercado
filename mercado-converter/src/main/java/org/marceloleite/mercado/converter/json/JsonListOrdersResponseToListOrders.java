package org.marceloleite.mercado.converter.json;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.jsonmodel.JsonListOrdersResponse;
import org.marceloleite.mercado.jsonmodel.JsonOrder;
import org.marceloleite.mercado.negotiationapi.model.Order;

public class JsonListOrdersResponseToListOrders implements Converter<JsonListOrdersResponse, List<Order>> {

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
