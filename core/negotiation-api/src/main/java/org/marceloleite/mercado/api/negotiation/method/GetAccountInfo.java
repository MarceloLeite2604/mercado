package org.marceloleite.mercado.api.negotiation.method;

import org.marceloleite.mercado.api.negotiation.model.AccountInfo;
import org.marceloleite.mercado.model.TapiInformation;
import org.springframework.core.ParameterizedTypeReference;

public class GetAccountInfo extends TapiMethodTemplate<TapiResponse<AccountInfo>> {

	public GetAccountInfo(TapiInformation tapiInformation) {
		super(tapiInformation, TapiMethod.GET_ACCOUNT_INFO, null,
				new ParameterizedTypeReference<TapiResponse<AccountInfo>>() {
				});
	}

	public TapiResponse<AccountInfo> execute() {
		return executeMethod();
	}
}
