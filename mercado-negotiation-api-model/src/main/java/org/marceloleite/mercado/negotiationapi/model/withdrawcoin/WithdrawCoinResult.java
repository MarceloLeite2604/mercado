package org.marceloleite.mercado.negotiationapi.model.withdrawcoin;

import org.marceloleite.mercado.negotiationapi.model.getwithdrawal.Withdrawal;

public class WithdrawCoinResult {

	private Withdrawal withdrawal;

	public WithdrawCoinResult(Withdrawal withdrawal) {
		super();
		this.withdrawal = withdrawal;
	}

	public WithdrawCoinResult() {
		this(null);
	}

	public Withdrawal getWithdrawal() {
		return withdrawal;
	}

	public void setWithdrawal(Withdrawal withdrawal) {
		this.withdrawal = withdrawal;
	}

}
