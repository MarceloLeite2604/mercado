package org.marceloleite.mercado.negotiationapi.model.getaccountinfo;

public class AccountInfo {

	private BalanceApi balanceApi;
	
	private WithdrawalLimits withdrawalLimits;

	public AccountInfo(BalanceApi balanceApi, WithdrawalLimits withdrawalLimits) {
		super();
		this.balanceApi = balanceApi;
		this.withdrawalLimits = withdrawalLimits;
	}
	
	public AccountInfo() {
		this(null, null);
	}

	public BalanceApi getBalanceApi() {
		return balanceApi;
	}

	public void setBalanceApi(BalanceApi balanceApi) {
		this.balanceApi = balanceApi;
	}

	public WithdrawalLimits getWithdrawalLimits() {
		return withdrawalLimits;
	}

	public void setWithdrawalLimits(WithdrawalLimits withdrawalLimits) {
		this.withdrawalLimits = withdrawalLimits;
	}
	
}
