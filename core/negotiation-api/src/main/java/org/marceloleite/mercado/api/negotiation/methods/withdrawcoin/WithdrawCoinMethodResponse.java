package org.marceloleite.mercado.api.negotiation.methods.withdrawcoin;

import org.marceloleite.mercado.api.negotiation.converters.JsonGetWithdrawalResponseToGetWithdrawalResponseConverter;
import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiResponse;
import org.marceloleite.mercado.api.negotiation.methods.getwithdrawal.GetWithdrawalResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.JsonTapiResponse;
import org.marceloleite.mercado.jsonmodel.api.negotiation.getwithdrawal.JsonGetWithdrawalResponse;

public class WithdrawCoinMethodResponse extends AbstractTapiResponse<JsonGetWithdrawalResponse, GetWithdrawalResponse> {

	public WithdrawCoinMethodResponse(JsonTapiResponse jsonTapiResponse) {
		super(jsonTapiResponse, JsonGetWithdrawalResponse.class,
				new JsonGetWithdrawalResponseToGetWithdrawalResponseConverter());
	}

}
