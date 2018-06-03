package org.marceloleite.mercado.api.negotiation.method;

import org.marceloleite.mercado.CurrencyPair;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.TapiInformation;
import org.springframework.core.ParameterizedTypeReference;

public class GetOrder extends TapiMethodTemplate<TapiResponse<Order>> {
	
	private static final String[] PARAMETER_NAMES = {"coin_pair", "order_id"};

	public GetOrder(TapiInformation tapiInformation) {
		super(tapiInformation, TapiMethod.GET_ORDER, PARAMETER_NAMES, new ParameterizedTypeReference<TapiResponse<Order>>(){});
	}

	public TapiResponse<Order> execute(CurrencyPair currencyPair, long orderId) {
		return executeMethod(currencyPair, orderId);
	}
}
