package org.marceloleite.mercado.api.negotiation.methods.getwithdrawal;

import org.marceloleite.mercado.api.negotiation.converters.JsonGetWithdrawalResponseToGetWithdrawalResponseConverter;
import org.marceloleite.mercado.api.negotiation.methods.TapiResponseTemplate;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.getwithdrawal.JsonGetWithdrawalResponse;

public class GetWithdrawalMethodResponse extends TapiResponseTemplate<JsonGetWithdrawalResponse, GetWithdrawalResponse>{

	public GetWithdrawalMethodResponse(NapiResponse jsonTapiResponse) {
		super(jsonTapiResponse, JsonGetWithdrawalResponse.class, new JsonGetWithdrawalResponseToGetWithdrawalResponseConverter());
	}
}
