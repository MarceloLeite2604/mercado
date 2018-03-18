	package org.marceloleite.mercado.api.negotiation.methods.placebuyorder;

import org.marceloleite.mercado.api.negotiation.checks.placebuyorder.PlaceBuyOrderQuantityCheck;
import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethod;
import org.marceloleite.mercado.base.model.TapiInformation;
import org.marceloleite.mercado.negotiationapi.model.CurrencyPair;

public class PlaceBuyOrderMethod extends AbstractTapiMethod<PlaceBuyOrderMethodResponse> {
	
	private static final String[] PARAMETER_NAMES = {"coin_pair", "quantity", "limit_price"};

	public PlaceBuyOrderMethod(TapiInformation tapiInformation) {
		super(tapiInformation, TapiMethod.PLACE_BUY_ORDER, PlaceBuyOrderMethodResponse.class, PARAMETER_NAMES);
	}
	
	public PlaceBuyOrderMethodResponse execute(CurrencyPair currencyPair, Double quantity, Double limitPrice) {
		checkQuantityParameter(currencyPair, quantity);
		/* TODO: Check limit price parameter. */
		return executeMethod(currencyPair, quantity, limitPrice);
	}

	private void checkQuantityParameter(CurrencyPair currencyPair, Double quantity) {
		PlaceBuyOrderQuantityCheck placeBuyOrderQuantityCheck = new PlaceBuyOrderQuantityCheck(
				currencyPair.getSecondCurrency());
		if (placeBuyOrderQuantityCheck.check(quantity)) {
			throw new RuntimeException("Quantity " + quantity + " is below minimal accepted value for \""
					+ currencyPair.getSecondCurrency() + "\" currency.");
		}
	}
}
