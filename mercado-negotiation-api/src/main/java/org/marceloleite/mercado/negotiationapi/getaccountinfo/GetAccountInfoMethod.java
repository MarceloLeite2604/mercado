package org.marceloleite.mercado.negotiationapi.getaccountinfo;

import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.negotiationapi.AbstractTapiMethod;
import org.marceloleite.mercado.negotiationapi.TapiMethod;

public class GetAccountInfoMethod extends AbstractTapiMethod<GetAccountInfoMethodResponse> {
	
	public GetAccountInfoMethodResponse execute() {
		return connectAndReadResponse(generateTapiMethodParameters());
	}

	@Override
	protected TapiMethod getTapiMethod() {
		return TapiMethod.GET_ACCOUNT_INFO;
	}

	@Override
	protected GetAccountInfoMethodResponse generateMethodResponse(JsonTapiResponse jsonTapiResponse) {
		return new GetAccountInfoMethodResponse(jsonTapiResponse);
	}
}
