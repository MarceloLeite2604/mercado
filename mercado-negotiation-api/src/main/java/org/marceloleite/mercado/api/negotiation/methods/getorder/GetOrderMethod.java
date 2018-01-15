package org.marceloleite.mercado.api.negotiation.methods.getorder;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethodParameters;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.negotiationapi.model.listorders.CurrencyPair;

public class GetOrderMethod extends AbstractTapiMethod<GetOrderMethodResponse> {

	public GetOrderMethodResponse execute(CurrencyPair currencyPair, long orderId) {
		TapiMethodParameters tapiMethodParameters = generateTapiMethodParameters(currencyPair, orderId);
		return connectAndReadResponse(tapiMethodParameters);
	}

	private TapiMethodParameters generateTapiMethodParameters(CurrencyPair currencyPair, long orderId) {
		TapiMethodParameters tapiMethodParameters = generateTapiMethodParameters();
		tapiMethodParameters.put(GetOrderParameters.COIN_PAIR.toString(), currencyPair);
		tapiMethodParameters.put(GetOrderParameters.ORDER_ID.toString(), Long.toString(orderId));
		return tapiMethodParameters;
	}

	@Override
	protected TapiMethod getTapiMethod() {
		return TapiMethod.GET_ORDER;
	}

	@Override
	protected GetOrderMethodResponse generateMethodResponse(JsonTapiResponse jsonTapiResponse) {
		return new GetOrderMethodResponse(jsonTapiResponse);
	}

}
