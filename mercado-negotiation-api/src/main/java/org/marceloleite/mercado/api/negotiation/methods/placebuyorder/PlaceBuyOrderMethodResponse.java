package org.marceloleite.mercado.api.negotiation.methods.placebuyorder;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiResponse;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.converter.json.api.negotiation.JsonPlaceBuyOrderResponseToPlaceBuyOrderResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonPlaceBuyOrderResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.negotiationapi.model.placebuyorder.PlaceBuyOrderResponse;

public class PlaceBuyOrderMethodResponse
		extends AbstractTapiResponse<JsonPlaceBuyOrderResponse, PlaceBuyOrderResponse> {

	private PlaceBuyOrderResponse placeBuyOrderResponse;

	public PlaceBuyOrderMethodResponse(JsonTapiResponse jsonTapiResponse) {
		super(jsonTapiResponse);
		this.placeBuyOrderResponse = getFormattedResponseData();
	}

	public PlaceBuyOrderResponse getPlaceBuyOrderResponse() {
		return placeBuyOrderResponse;
	}

	@Override
	protected Class<?> getJsonResponseDataClass() {
		return JsonPlaceBuyOrderResponse.class;
	}

	@Override
	protected Converter<JsonPlaceBuyOrderResponse, PlaceBuyOrderResponse> getConverter() {
		return new JsonPlaceBuyOrderResponseToPlaceBuyOrderResponse();
	}

}
