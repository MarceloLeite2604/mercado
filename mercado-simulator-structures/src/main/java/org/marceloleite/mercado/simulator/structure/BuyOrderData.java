package org.marceloleite.mercado.simulator.structure;

import java.time.LocalDateTime;

public class BuyOrderData {

	private LocalDateTime time;

	private CurrencyAmount currencyAmountToBuy;

	private CurrencyAmount currencyAmountToPay;

	public BuyOrderData() {
		this(null, null, null);
	}

	public BuyOrderData(LocalDateTime time, CurrencyAmount currencyAmountToBuy, CurrencyAmount currencyAmountToPay) {
		super();
		this.time = time;
		this.currencyAmountToBuy = currencyAmountToBuy;
		this.currencyAmountToPay = currencyAmountToPay;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public CurrencyAmount getCurrencyAmountToBuy() {
		return currencyAmountToBuy;
	}

	public void setCurrencyAmountToBuy(CurrencyAmount currencyAmountToBuy) {
		this.currencyAmountToBuy = currencyAmountToBuy;
	}

	public CurrencyAmount getCurrencyAmountToPay() {
		return currencyAmountToPay;
	}

	public void setCurrencyAmountToPay(CurrencyAmount currencyAmountToPay) {
		this.currencyAmountToPay = currencyAmountToPay;
	}
}
