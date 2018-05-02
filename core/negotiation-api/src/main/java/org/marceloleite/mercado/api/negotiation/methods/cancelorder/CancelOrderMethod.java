package org.marceloleite.mercado.api.negotiation.methods.cancelorder;

import org.marceloleite.mercado.api.negotiation.CurrencyPair;
import org.marceloleite.mercado.api.negotiation.TapiResponse;
import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethod;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.TapiInformation;

public class CancelOrderMethod extends AbstractTapiMethod<TapiResponse<Order>> {

	private static final String[] PARAMETER_NAMES = { "coin_pair", "order_id" };

	public CancelOrderMethod(TapiInformation tapiInformation) {
		super(tapiInformation, TapiMethod.CANCEL_ORDER, PARAMETER_NAMES);
	}

	public TapiResponse<Order> execute(CurrencyPair currencyPair, Long orderId) {
		return executeMethod(currencyPair, orderId);
	}
}
