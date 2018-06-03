package org.marceloleite.mercado.api.negotiation.method;

import org.marceloleite.mercado.api.negotiation.model.Withdrawal;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.model.TapiInformation;
import org.springframework.core.ParameterizedTypeReference;

public class WithdrawCoin extends TapiMethodTemplate<TapiResponse<Withdrawal>> {

	private static final String[] PARAMETER_NAMES = { "coin", "withdrawal_id" };

	public WithdrawCoin(TapiInformation tapiInformation) {
		super(tapiInformation, TapiMethod.WITHDRAW_COIN, PARAMETER_NAMES,
				new ParameterizedTypeReference<TapiResponse<Withdrawal>>() {
				});
	}

	public TapiResponse<Withdrawal> execute(Currency coin, Long withdrawalId) {
		return executeMethod(coin, withdrawalId);
	}

}
