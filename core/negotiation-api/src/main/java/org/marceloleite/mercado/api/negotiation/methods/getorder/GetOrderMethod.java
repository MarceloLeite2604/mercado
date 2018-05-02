package org.marceloleite.mercado.api.negotiation.methods.getorder;

import org.marceloleite.mercado.api.negotiation.CurrencyPair;
import org.marceloleite.mercado.api.negotiation.TapiResponse;
import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethod;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.TapiInformation;

public class GetOrderMethod extends AbstractTapiMethod<TapiResponse<Order>> {
	
	private static final String[] PARAMETER_NAMES = {"coin_pair", "order_id"};

	public GetOrderMethod(TapiInformation tapiInformation) {
		super(tapiInformation, TapiMethod.GET_ORDER, PARAMETER_NAMES);
	}

	public TapiResponse<Order> execute(CurrencyPair currencyPair, long orderId) {
		return executeMethod(currencyPair, orderId);
	}
}
