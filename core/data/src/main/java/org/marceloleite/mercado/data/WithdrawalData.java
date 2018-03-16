package org.marceloleite.mercado.data;

import org.marceloleite.mercado.commons.Currency;

public class WithdrawalData {

	private AccountData accountData;

	private Currency currency;

	private Double amount;

	public AccountData getAccountData() {
		return accountData;
	}

	public void setAccountData(AccountData accountData) {
		this.accountData = accountData;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
