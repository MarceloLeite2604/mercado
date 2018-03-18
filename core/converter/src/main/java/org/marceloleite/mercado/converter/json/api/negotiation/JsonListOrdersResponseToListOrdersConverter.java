package org.marceloleite.mercado.converter.json.api.negotiation;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.OrderData;
import org.marceloleite.mercado.jsonmodel.api.data.JsonOrder;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonListOrdersResponse;

public class JsonListOrdersResponseToListOrdersConverter implements Converter<JsonListOrdersResponse, List<OrderData>> {

	@Override
	public List<OrderData> convertTo(JsonListOrdersResponse jsonListOrdersResponse) {
		List<OrderData> orderDatas = new ArrayList<>();
		List<JsonOrder> jsonOrders = jsonListOrdersResponse.getOrders();
		if (jsonOrders != null && !jsonOrders.isEmpty()) {
			JsonOrderToOrderDataConverter jsonOrderToOrderDataConverter = new JsonOrderToOrderDataConverter();
			for (JsonOrder jsonOrder : jsonOrders) {
				orderDatas.add(jsonOrderToOrderDataConverter.convertTo(jsonOrder));
			}
		}
		return orderDatas;
	}

	@Override
	public JsonListOrdersResponse convertFrom(List<OrderData> orderDatas) {
		throw new UnsupportedOperationException();
	}

}
