package org.marceloleite.mercado.api.negotiation.methods.cancelorder;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethod;
import org.marceloleite.mercado.negotiationapi.model.CurrencyPair;

public class CancelOrderMethod extends AbstractTapiMethod<CancelOrderMethodResponse> {
	
	private static final String[] PARAMETER_NAMES = {"coin_pair", "order_id"};

	public CancelOrderMethod(TapiMethod tapiMethod) {
		super(tapiMethod, CancelOrderMethodResponse.class, PARAMETER_NAMES);
	}
	
	public CancelOrderMethodResponse execute(CurrencyPair currencyPair, Long orderId) {
		return executeMethod(currencyPair, orderId);
	}
}
