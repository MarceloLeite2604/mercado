package org.marceloleite.mercado.api.negotiation.methods.getwithdrawal;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiResponse;
import org.marceloleite.mercado.converter.json.api.negotiation.getwithdrawal.JsonGetWithdrawalResponseToGetWithdrawalResponseConverter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.getwithdrawal.JsonGetWithdrawalResponse;
import org.marceloleite.mercado.negotiationapi.model.getwithdrawal.GetWithdrawalResponse;

public class GetWithdrawalMethodResponse extends AbstractTapiResponse<JsonGetWithdrawalResponse, GetWithdrawalResponse>{

	public GetWithdrawalMethodResponse(JsonTapiResponse jsonTapiResponse) {
		super(jsonTapiResponse, JsonGetWithdrawalResponse.class, new JsonGetWithdrawalResponseToGetWithdrawalResponseConverter());
	}
}
