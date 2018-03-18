package org.marceloleite.mercado.api.negotiation.methods.withdrawcoin;

import org.marceloleite.mercado.negotiationapi.model.getwithdrawal.Withdrawal;

public class WithdrawCoinResponse {

	private Withdrawal withdrawal;

	public WithdrawCoinResponse(Withdrawal withdrawal) {
		super();
		this.withdrawal = withdrawal;
	}

	public WithdrawCoinResponse() {
		this(null);
	}

	public Withdrawal getWithdrawal() {
		return withdrawal;
	}

	public void setWithdrawal(Withdrawal withdrawal) {
		this.withdrawal = withdrawal;
	}

}
