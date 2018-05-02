package org.marceloleite.mercado.api.negotiation.methods;

import org.marceloleite.mercado.api.negotiation.TapiResponse;
import org.marceloleite.mercado.api.negotiation.model.Withdrawal;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.model.TapiInformation;

public class GetWithdrawal extends TapiMethodTemplate<TapiResponse<Withdrawal>> {
	
	private static final String[] PARAMETER_NAMES = {"coin", "withdrawal_id"};

	public GetWithdrawal(TapiInformation tapiInformation) {
		super(tapiInformation, TapiMethod.GET_WITHDRAWAL, PARAMETER_NAMES);
	}
	
	public TapiResponse<Withdrawal> execute(Currency coin, Long withdrawalId) {
		return executeMethod(coin, withdrawalId);
	}
}
