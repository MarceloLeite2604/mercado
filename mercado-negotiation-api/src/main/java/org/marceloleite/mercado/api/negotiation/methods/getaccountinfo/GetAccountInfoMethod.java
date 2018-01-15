package org.marceloleite.mercado.api.negotiation.methods.getaccountinfo;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethod;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;

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
