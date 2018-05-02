package org.marceloleite.mercado.api.negotiation.methods.getaccountinfo;

import org.marceloleite.mercado.api.negotiation.TapiResponse;
import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.getaccountinfo.response.AccountInfo;
import org.marceloleite.mercado.model.TapiInformation;

public class GetAccountInfoMethod extends AbstractTapiMethod<TapiResponse<AccountInfo>> {

	public GetAccountInfoMethod(TapiInformation tapiInformation) {
		super(tapiInformation, TapiMethod.GET_ACCOUNT_INFO, null);
	}

	public TapiResponse<AccountInfo> execute() {
		return executeMethod();
	}
}
