package org.marceloleite.mercado.simulator.structure;

import java.time.LocalDateTime;

public class BuyOrderData {

	private LocalDateTime time;

	private CurrencyAmountData currencyAmountToBuy;

	private CurrencyAmountData currencyAmountToPay;

	public BuyOrderData() {
		this(null, null, null);
	}

	public BuyOrderData(LocalDateTime time, CurrencyAmountData currencyAmountToBuy, CurrencyAmountData currencyAmountToPay) {
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

	public CurrencyAmountData getCurrencyAmountToBuy() {
		return currencyAmountToBuy;
	}

	public void setCurrencyAmountToBuy(CurrencyAmountData currencyAmountToBuy) {
		this.currencyAmountToBuy = currencyAmountToBuy;
	}

	public CurrencyAmountData getCurrencyAmountToPay() {
		return currencyAmountToPay;
	}

	public void setCurrencyAmountToPay(CurrencyAmountData currencyAmountToPay) {
		this.currencyAmountToPay = currencyAmountToPay;
	}
}
