package org.marceloleite.mercado.api.negotiation.methods.getaccountinfo;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethod;

public class GetAccountInfoMethod extends AbstractTapiMethod<GetAccountInfoMethodResponse> {

	public GetAccountInfoMethod() {
		super(TapiMethod.GET_ACCOUNT_INFO, GetAccountInfoMethodResponse.class, null);
	}

	public GetAccountInfoMethodResponse execute() {
		return executeMethod();
	}
}
