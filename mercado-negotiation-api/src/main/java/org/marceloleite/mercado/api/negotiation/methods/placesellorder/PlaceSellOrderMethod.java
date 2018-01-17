package org.marceloleite.mercado.api.negotiation.methods.placesellorder;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethod;
import org.marceloleite.mercado.negotiationapi.model.CurrencyPair;

public class PlaceSellOrderMethod extends AbstractTapiMethod<PlaceSellOrderMethodResult> {
	
	private static final String[] PARAMETER_NAMES = {"coin_pair", "quantity", "limit_price"};
	
	public PlaceSellOrderMethod() {
		super(TapiMethod.PLACE_SELL_ORDER, PlaceSellOrderMethodResult.class, PARAMETER_NAMES);
	}

	public PlaceSellOrderMethodResult execute(CurrencyPair currencyPair, Double quantity, Double limitPrice) {
		return executeMethod(currencyPair, quantity, limitPrice);
	}
}
