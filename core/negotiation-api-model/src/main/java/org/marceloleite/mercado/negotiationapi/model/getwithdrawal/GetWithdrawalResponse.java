package org.marceloleite.mercado.negotiationapi.model.getwithdrawal;

public class GetWithdrawalResponse {

	private Withdrawal withdrawal;

	public GetWithdrawalResponse(Withdrawal withdrawal) {
		super();
		this.withdrawal = withdrawal;
	}

	public GetWithdrawalResponse() {
		this(null);
	}

	public Withdrawal getWithdrawal() {
		return withdrawal;
	}

	public void setWithdrawal(Withdrawal withdrawal) {
		this.withdrawal = withdrawal;
	}
}
