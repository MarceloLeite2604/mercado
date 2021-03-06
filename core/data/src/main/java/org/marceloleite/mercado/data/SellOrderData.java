package org.marceloleite.mercado.data;

import java.time.ZonedDateTime;

public class SellOrderData {

	private ZonedDateTime time;
	
	private AccountData accountData;

	private CurrencyAmountData currencyAmountToSell;

	private CurrencyAmountData currencyAmountToReceive;

	public SellOrderData() {
		this(null, null, null);
	}

	public SellOrderData(ZonedDateTime time, CurrencyAmountData currencyAmountToSell, CurrencyAmountData currencyAmountToReceive) {
		super();
		this.time = time;
		this.currencyAmountToSell = currencyAmountToSell;
		this.currencyAmountToReceive = currencyAmountToReceive;
	}

	public ZonedDateTime getTime() {
		return time;
	}

	public void setTime(ZonedDateTime time) {
		this.time = time;
	}

	public CurrencyAmountData getCurrencyAmountToSell() {
		return currencyAmountToSell;
	}

	public void setCurrencyAmountToSell(CurrencyAmountData currencyAmountToSell) {
		this.currencyAmountToSell = currencyAmountToSell;
	}

	public CurrencyAmountData getCurrencyAmountToReceive() {
		return currencyAmountToReceive;
	}

	public void setCurrencyAmountToReceive(CurrencyAmountData currencyAmountToReceive) {
		this.currencyAmountToReceive = currencyAmountToReceive;
	}

	public AccountData getAccountData() {
		return accountData;
	}

	public void setAccountData(AccountData accountData) {
		this.accountData = accountData;
	}
}
