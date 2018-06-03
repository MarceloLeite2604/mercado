package org.marceloleite.mercado.api.negotiation.method;

import org.marceloleite.mercado.api.negotiation.model.Withdrawal;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.model.Order;
import org.marceloleite.mercado.model.TapiInformation;
import org.springframework.core.ParameterizedTypeReference;

public class GetWithdrawal extends TapiMethodTemplate<TapiResponse<Withdrawal>> {
	
	private static final String[] PARAMETER_NAMES = {"coin", "withdrawal_id"};

	public GetWithdrawal(TapiInformation tapiInformation) {
		super(tapiInformation, TapiMethod.GET_WITHDRAWAL, PARAMETER_NAMES, new ParameterizedTypeReference<TapiResponse<Withdrawal>>(){});
	}
	
	public TapiResponse<Withdrawal> execute(Currency coin, Long withdrawalId) {
		return executeMethod(coin, withdrawalId);
	}
}
