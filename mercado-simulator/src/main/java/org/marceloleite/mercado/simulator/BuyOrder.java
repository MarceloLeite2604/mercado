package org.marceloleite.mercado.simulator;

import java.time.LocalDateTime;

import org.marceloleite.mercado.commons.Currency;

public class BuyOrder {

	private Currency currencyToBuy;
	
	private double buyingAmount;
	
	private Currency currencyToSpend;
	
	private double spendAmount;

	private LocalDateTime time;

	public BuyOrder() {
		super();
	}

	public Currency getCurrencyToBuy() {
		return currencyToBuy;
	}

	public void setCurrencyToBuy(Currency currencyToBuy) {
		this.currencyToBuy = currencyToBuy;
	}

	public double getBuyingAmount() {
		return buyingAmount;
	}

	public void setBuyingAmount(double buyingAmount) {
		this.buyingAmount = buyingAmount;
	}

	public Currency getCurrencyToSpend() {
		return currencyToSpend;
	}

	public void setCurrencyToSpend(Currency currencyToSpend) {
		this.currencyToSpend = currencyToSpend;
	}

	public double getSpendAmount() {
		return spendAmount;
	}

	public void setSpendAmount(double spendAmount) {
		this.spendAmount = spendAmount;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}
}
