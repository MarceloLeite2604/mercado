package org.marceloleite.mercado.api.negotiation.methods.placesellorder;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethod;
import org.marceloleite.mercado.base.model.TapiInformation;
import org.marceloleite.mercado.negotiationapi.model.CurrencyPair;

public class PlaceSellOrderMethod extends AbstractTapiMethod<PlaceSellOrderMethodResponse> {
	
	private static final String[] PARAMETER_NAMES = {"coin_pair", "quantity", "limit_price"};
	
	public PlaceSellOrderMethod(TapiInformation tapiInformation) {
		super(tapiInformation, TapiMethod.PLACE_SELL_ORDER, PlaceSellOrderMethodResponse.class, PARAMETER_NAMES);
	}

	public PlaceSellOrderMethodResponse execute(CurrencyPair currencyPair, Double quantity, Double limitPrice) {
		return executeMethod(currencyPair, quantity, limitPrice);
	}
}
