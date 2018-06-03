package org.marceloleite.mercado.api.negotiation.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountInfo {

	private Currencies balance;
	
	@JsonProperty("withdrawal_limits")
	private Currencies withdrawalLimits;

	public Currencies getBalance() {
		return balance;
	}

	public void setBalance(Currencies balance) {
		this.balance = balance;
	}

	public Currencies getWithdrawalLimits() {
		return withdrawalLimits;
	}

	public void setWithdrawalLimits(Currencies withdrawalLimits) {
		this.withdrawalLimits = withdrawalLimits;
	}

	
}
