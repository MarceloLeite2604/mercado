package org.marceloleite.mercado.simulator.data;

import java.time.ZonedDateTime;

public class BuyOrderData {

	private ZonedDateTime time;

	private CurrencyAmountData currencyAmountToBuy;

	private CurrencyAmountData currencyAmountToPay;

	public BuyOrderData() {
		this(null, null, null);
	}

	public BuyOrderData(ZonedDateTime time, CurrencyAmountData currencyAmountToBuy, CurrencyAmountData currencyAmountToPay) {
		super();
		this.time = time;
		this.currencyAmountToBuy = currencyAmountToBuy;
		this.currencyAmountToPay = currencyAmountToPay;
	}

	public ZonedDateTime getTime() {
		return time;
	}

	public void setTime(ZonedDateTime time) {
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
