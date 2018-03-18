package org.marceloleite.mercado.converter.json.api.negotiation;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.data.OrderData;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonGetOrderResponse;
import org.marceloleite.mercado.negotiationapi.model.getorder.GetOrderResponse;

public class JsonGetOrderResponseToGetOrderResponseConverter
		implements Converter<JsonGetOrderResponse, GetOrderResponse> {

	@Override
	public GetOrderResponse convertTo(JsonGetOrderResponse jsonGetOrderResponse) {
		GetOrderResponse getOrderResponse = new GetOrderResponse();
		JsonOrderToOrderDataConverter jsonOrderToOrderConverter = new JsonOrderToOrderDataConverter();
		OrderData orderData = jsonOrderToOrderConverter.convertTo(jsonGetOrderResponse.getOrder());
		getOrderResponse.setOrder(orderData);
		return getOrderResponse;
	}

	@Override
	public JsonGetOrderResponse convertFrom(GetOrderResponse getOrderResponse) {
		throw new UnsupportedOperationException();
	}

}
