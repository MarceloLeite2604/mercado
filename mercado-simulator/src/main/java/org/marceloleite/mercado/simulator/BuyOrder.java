package org.marceloleite.mercado.simulator;

import java.time.LocalDateTime;

public class BuyOrder {

	private CurrencyAmount currencyAmountToBuy;

	private CurrencyAmount currencyAmountToSpend;

	private LocalDateTime time;

	public BuyOrder(CurrencyAmount currencyAmountToBuy, CurrencyAmount currencyAmountToSpend, LocalDateTime time) {
		super();
		this.currencyAmountToBuy = currencyAmountToBuy;
		this.currencyAmountToSpend = currencyAmountToSpend;
		this.time = time;
	}

	public CurrencyAmount getCurrencyAmountToBuy() {
		return currencyAmountToBuy;
	}

	public void setCurrencyAmountToBuy(CurrencyAmount currencyAmountToBuy) {
		this.currencyAmountToBuy = currencyAmountToBuy;
	}

	public CurrencyAmount getCurrencyAmountToSpend() {
		return currencyAmountToSpend;
	}

	public void setCurrencyAmountToSpend(CurrencyAmount currencyAmountToSpend) {
		this.currencyAmountToSpend = currencyAmountToSpend;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public boolean isBuyingOrder() {
		return (currencyAmountToBuy != null);
	}

}
