package org.marceloleite.mercado.api.negotiation.methods.withdrawcoin;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiResponse;
import org.marceloleite.mercado.converter.json.api.negotiation.getwithdrawal.JsonGetWithdrawalResponseToGetWithdrawalResponseConverter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.getwithdrawal.JsonGetWithdrawalResponse;
import org.marceloleite.mercado.negotiationapi.model.getwithdrawal.GetWithdrawalResponse;

public class WithdrawCoinMethodResponse extends AbstractTapiResponse<JsonGetWithdrawalResponse, GetWithdrawalResponse> {

	public WithdrawCoinMethodResponse(JsonTapiResponse jsonTapiResponse) {
		super(jsonTapiResponse, JsonGetWithdrawalResponse.class,
				new JsonGetWithdrawalResponseToGetWithdrawalResponseConverter());
	}

}
