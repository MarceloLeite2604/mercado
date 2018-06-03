package org.marceloleite.mercado.api.negotiation.method;

import org.marceloleite.mercado.CurrencyPair;
import org.marceloleite.mercado.api.negotiation.model.Orderbook;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.TapiInformation;
import org.springframework.core.ParameterizedTypeReference;

public class ListOrderbook extends TapiMethodTemplate<TapiResponse<Orderbook>> {
	
	private static final String[] PARAMETER_NAMES = {"coin_pair", "full"};

	public ListOrderbook(TapiInformation tapiInformation) {
		super(tapiInformation, TapiMethod.LIST_ORDERBOOK, PARAMETER_NAMES, new ParameterizedTypeReference<TapiResponse<Orderbook>>(){});
	}

	public TapiResponse<Orderbook> execute(CurrencyPair currencyPair, Boolean full) {
		return executeMethod(currencyPair, full);
	}

	public TapiResponse<Orderbook> execute(CurrencyPair currencyPair) {
		return execute(currencyPair, null);
	}
}
