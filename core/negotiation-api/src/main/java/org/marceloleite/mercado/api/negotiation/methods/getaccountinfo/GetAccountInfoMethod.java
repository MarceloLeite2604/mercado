package org.marceloleite.mercado.api.negotiation.methods.getaccountinfo;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethod;
import org.marceloleite.mercado.base.model.TapiInformation;

public class GetAccountInfoMethod extends AbstractTapiMethod<GetAccountInfoMethodResponse> {

	public GetAccountInfoMethod(TapiInformation tapiInformation) {
		super(tapiInformation, TapiMethod.GET_ACCOUNT_INFO, GetAccountInfoMethodResponse.class, null);
	}

	public GetAccountInfoMethodResponse execute() {
		return executeMethod();
	}
}
