package org.marceloleite.mercado.api.negotiation.methods.getorder;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethod;
import org.marceloleite.mercado.negotiationapi.model.CurrencyPair;

public class GetOrderMethod extends AbstractTapiMethod<GetOrderMethodResponse> {
	
	private static final String[] PARAMETER_NAMES = {"coin_pair", "order_id"};

	public GetOrderMethod() {
		super(TapiMethod.GET_ORDER, GetOrderMethodResponse.class, PARAMETER_NAMES);
	}

	public GetOrderMethodResponse execute(CurrencyPair currencyPair, long orderId) {
		return executeMethod(currencyPair, orderId);
	}
}
