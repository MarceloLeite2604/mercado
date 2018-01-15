package org.marceloleite.mercado.api.negotiation.methods.getorder;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiResponse;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.converter.json.JsonGetOrderResponseToGetOrderResponseConverter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonGetOrderResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.negotiationapi.model.getorder.GetOrderResponse;

public class GetOrderMethodResponse extends AbstractTapiResponse<JsonGetOrderResponse, GetOrderResponse> {

	private GetOrderResponse getOrderResponse;

	public GetOrderMethodResponse(JsonTapiResponse jsonTapiResponse) {
		super(jsonTapiResponse);
		this.getOrderResponse = getFormattedResponseData();
	}

	public GetOrderResponse getGetOrderResponse() {
		return getOrderResponse;
	}

	@Override
	protected Class<?> getJsonResponseDataClass() {
		return JsonGetOrderResponse.class;
	}

	@Override
	protected Converter<JsonGetOrderResponse, GetOrderResponse> getConverter() {
		return new JsonGetOrderResponseToGetOrderResponseConverter();
	}

}
