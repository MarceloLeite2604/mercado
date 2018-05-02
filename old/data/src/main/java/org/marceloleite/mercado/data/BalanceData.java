package org.marceloleite.mercado.data;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;

public class BalanceData {

	private AccountData accountData;

	private Currency currency;

	private MercadoBigDecimal amount;

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

	public MercadoBigDecimal getAmount() {
		return amount;
	}

	public void setAmount(MercadoBigDecimal amount) {
		this.amount = amount;
	}
}
