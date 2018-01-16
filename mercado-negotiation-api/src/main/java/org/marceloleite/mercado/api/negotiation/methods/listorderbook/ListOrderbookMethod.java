package org.marceloleite.mercado.api.negotiation.methods.listorderbook;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethodParameters;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.negotiationapi.model.listorders.CurrencyPair;

public class ListOrderbookMethod extends AbstractTapiMethod<ListOrderbookMethodResponse> {

	@Override
	protected TapiMethod getTapiMethod() {
		return TapiMethod.LIST_ORDERBOOK;
	}

	@Override
	protected ListOrderbookMethodResponse generateMethodResponse(JsonTapiResponse jsonTapiResponse) {
		return new ListOrderbookMethodResponse(jsonTapiResponse);
	}

	public ListOrderbookMethodResponse execute(CurrencyPair currencyPair, Boolean full) {
		TapiMethodParameters tapiMethodParameters = generateTapiMethodParameters(currencyPair, full);
		return connectAndReadResponse(tapiMethodParameters);
	}

	public ListOrderbookMethodResponse execute(CurrencyPair currencyPair) {
		return execute(currencyPair, null);
	}

	private TapiMethodParameters generateTapiMethodParameters(CurrencyPair currencyPair, Boolean full) {
		TapiMethodParameters tapiMethodParameters = generateTapiMethodParameters();
		tapiMethodParameters.put(ListOrderbookParameters.COIN_PAIR.toString(), currencyPair);
		tapiMethodParameters.put(ListOrderbookParameters.FULL.toString(), full);
		return tapiMethodParameters;
	}

}
