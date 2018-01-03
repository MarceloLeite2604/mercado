package org.marceloleite.mercado.simulator.structure;

import java.time.LocalDateTime;

import org.marceloleite.mercado.commons.Currency;

public class BuyOrder {

	private LocalDateTime time;

	private Currency currencyToBuy;

	private Double amountToBuy;

	private Currency currencyToPay;

	private Double amountToPay;

	public BuyOrder() {
		this(null, null, null, null, null);
	}

	private BuyOrder(LocalDateTime time, Currency currencyToBuy, Double amountToBuy, Currency currencyToPay,
			Double amountToPay) {
		super();
		this.time = time;
		this.currencyToBuy = currencyToBuy;
		this.amountToBuy = amountToBuy;
		this.currencyToPay = currencyToPay;
		this.amountToPay = amountToPay;
	}

	public BuyOrder(LocalDateTime time, Currency currencyToBuy, Double amountToBuy, Currency currencyToPay) {
		this(time, currencyToBuy, amountToBuy, currencyToPay, null);
	}

	public BuyOrder(LocalDateTime time, Currency currencyToBuy, Currency currencyToPay, Double amountToPay) {
		this(time, currencyToBuy, null, currencyToPay, amountToPay);
	}
	
	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public Currency getCurrencyToBuy() {
		return currencyToBuy;
	}

	public void setCurrencyToBuy(Currency currencyToBuy) {
		this.currencyToBuy = currencyToBuy;
	}

	public Double getAmountToBuy() {
		return amountToBuy;
	}

	public void setAmountToBuy(Double amountToBuy) {
		this.amountToBuy = amountToBuy;
	}

	public Currency getCurrencyToPay() {
		return currencyToPay;
	}

	public void setCurrencyToPay(Currency currencyToPay) {
		this.currencyToPay = currencyToPay;
	}

	public Double getAmountToPay() {
		return amountToPay;
	}

	public void setAmountToPay(Double amountToPay) {
		this.amountToPay = amountToPay;
	}
}
