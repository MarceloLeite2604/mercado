package org.marceloleite.mercado.data;

import java.math.BigDecimal;

import org.marceloleite.mercado.commons.Currency;

public class BalanceData {

	private AccountData accountData;

	private Currency currency;

	private BigDecimal amount;

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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
