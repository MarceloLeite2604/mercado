package org.marceloleite.mercado.api.negotiation.methods.getwithdrawal;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethod;
import org.marceloleite.mercado.commons.Currency;

public class GetWithdrawalMethod extends AbstractTapiMethod<GetWithdrawalMethodResponse> {
	
	private static final String[] PARAMETER_NAMES = {"coin", "withdrawal_id"};

	public GetWithdrawalMethod(TapiMethod tapiMethod, Class<?> responseClass) {
		super(tapiMethod, responseClass, PARAMETER_NAMES);
	}
	
	public GetWithdrawalMethodResponse execute(Currency coin, Long withdrawalId) {
		return executeMethod(coin, withdrawalId);
	}
}
