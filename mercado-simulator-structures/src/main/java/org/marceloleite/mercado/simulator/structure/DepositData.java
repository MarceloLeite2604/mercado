package org.marceloleite.mercado.simulator.structure;

import java.time.ZonedDateTime;

public class DepositData {
	
	private ZonedDateTime time;
	
	private CurrencyAmountData currencyAmount;
	
	public DepositData() {
		super();
	}
	
	public DepositData(ZonedDateTime time, CurrencyAmountData currencyAmount) {
		super();
		this.time = time;
		this.currencyAmount = currencyAmount;
	}

	public ZonedDateTime getTime() {
		return time;
	}

	public void setTime(ZonedDateTime time) {
		this.time = time;
	}

	public CurrencyAmountData getCurrencyAmount() {
		return currencyAmount;
	}
	
	public void setCurrencyAmount(CurrencyAmountData currencyAmount) {
		this.currencyAmount = currencyAmount;
	}
	
	
}
