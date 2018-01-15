package org.marceloleite.mercado.negotiationapi.model.accountinfo;

public class AccountInfo {

	private Balance balance;
	
	private WithdrawalLimits withdrawalLimits;

	public AccountInfo(Balance balance, WithdrawalLimits withdrawalLimits) {
		super();
		this.balance = balance;
		this.withdrawalLimits = withdrawalLimits;
	}
	
	public AccountInfo() {
		this(null, null);
	}

	public Balance getBalance() {
		return balance;
	}

	public void setBalance(Balance balance) {
		this.balance = balance;
	}

	public WithdrawalLimits getWithdrawalLimits() {
		return withdrawalLimits;
	}

	public void setWithdrawalLimits(WithdrawalLimits withdrawalLimits) {
		this.withdrawalLimits = withdrawalLimits;
	}
	
}
