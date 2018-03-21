package org.marceloleite.mercado.api.negotiation.converters;

import org.marceloleite.mercado.api.negotiation.methods.getorder.GetOrderResponse;
import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.converter.json.api.negotiation.JsonOrderToOrderDataConverter;
import org.marceloleite.mercado.data.OrderData;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonGetOrderResponse;

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