package org.marceloleite.mercado.simulator;

import java.time.LocalDateTime;

public class BuyRequest {
	
	private Balance balance;

	private CurrencyAmount currencyAmountToBuy;

	private LocalDateTime time;

	public BuyRequest() {
		super();
	}

	public Balance getBalance() {
		return balance;
	}

	public void setBalance(Balance balance) {
		this.balance = balance;
	}

	public CurrencyAmount getCurrencyAmountToBuy() {
		return currencyAmountToBuy;
	}

	public void setCurrencyAmountToBuy(CurrencyAmount currencyAmountToBuy) {
		this.currencyAmountToBuy = currencyAmountToBuy;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}
}
