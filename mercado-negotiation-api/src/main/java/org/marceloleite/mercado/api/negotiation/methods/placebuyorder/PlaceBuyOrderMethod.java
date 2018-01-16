package org.marceloleite.mercado.api.negotiation.methods.placebuyorder;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethodParameters;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.negotiationapi.model.listorders.CurrencyPair;

public class PlaceBuyOrderMethod extends AbstractTapiMethod<PlaceBuyOrderMethodResponse> {

	public PlaceBuyOrderMethodResponse execute(CurrencyPair currencyPair, Double quantity, Double limitPrice) {
		TapiMethodParameters tapiMethodParameters = generateTapiMethodParameters(currencyPair, quantity, limitPrice);
		return connectAndReadResponse(tapiMethodParameters);
	}

	private TapiMethodParameters generateTapiMethodParameters(CurrencyPair currencyPair, Double quantity,
			Double limitPrice) {
		TapiMethodParameters tapiMethodParameters = generateTapiMethodParameters();
		PlaceBuyOrderQuantityCheck placeBuyOrderQuantityCheck = new PlaceBuyOrderQuantityCheck(
				currencyPair.getSecondCurrency());
		if (placeBuyOrderQuantityCheck.check(quantity)) {
			throw new RuntimeException("Quantity " + quantity + " is below minimal accepted value for \""
					+ currencyPair.getSecondCurrency() + "\" currency.");
		}
		tapiMethodParameters.put(PlaceBuyOrderParameters.COIN_PAIR.toString(), currencyPair);
		tapiMethodParameters.put(PlaceBuyOrderParameters.QUANTITY.toString(), quantity);
		tapiMethodParameters.put(PlaceBuyOrderParameters.LIMIT_PRICE.toString(), limitPrice);
		return tapiMethodParameters;
	}

	@Override
	protected TapiMethod getTapiMethod() {
		return TapiMethod.PLACE_BUY_ORDER;
	}

	@Override
	protected PlaceBuyOrderMethodResponse generateMethodResponse(JsonTapiResponse jsonTapiResponse) {
		return new PlaceBuyOrderMethodResponse(jsonTapiResponse);
	}

}
