package org.marceloleite.mercado.api.negotiation.methods.listorderbook;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethod;
import org.marceloleite.mercado.base.model.TapiInformation;
import org.marceloleite.mercado.negotiationapi.model.CurrencyPair;

public class ListOrderbookMethod extends AbstractTapiMethod<ListOrderbookMethodResponse> {
	
	private static final String[] PARAMETER_NAMES = {"coin_pair", "full"};

	public ListOrderbookMethod(TapiInformation tapiInformation) {
		super(tapiInformation, TapiMethod.LIST_ORDERBOOK, ListOrderbookMethodResponse.class, PARAMETER_NAMES);
	}

	public ListOrderbookMethodResponse execute(CurrencyPair currencyPair, Boolean full) {
		return executeMethod(currencyPair, full);
	}

	public ListOrderbookMethodResponse execute(CurrencyPair currencyPair) {
		return execute(currencyPair, null);
	}
}
