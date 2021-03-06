package org.marceloleite.mercado.api.negotiation.methods.withdrawcoin;

import org.marceloleite.mercado.api.negotiation.methods.AbstractTapiMethod;
import org.marceloleite.mercado.api.negotiation.methods.TapiMethod;
import org.marceloleite.mercado.base.model.TapiInformation;
import org.marceloleite.mercado.commons.Currency;

public class WithdrawCoinMethod extends AbstractTapiMethod<WithdrawCoinMethodResponse> {

	private static final String[] PARAMETER_NAMES = { "coin", "withdrawal_id" };

	public WithdrawCoinMethod(TapiInformation tapiInformation) {
		super(tapiInformation, TapiMethod.WITHDRAW_COIN, WithdrawCoinMethodResponse.class, PARAMETER_NAMES);
	}
	
	public WithdrawCoinMethodResponse execute(Currency coin, Long withdrawalId) {
		return executeMethod(coin, withdrawalId);
	}

}
