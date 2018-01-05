package org.marceloleite.mercado.simulator.structure;

import java.time.LocalDateTime;

public class DepositData {
	
	private LocalDateTime time;
	
	private CurrencyAmountData currencyAmount;
	
	public DepositData() {
		super();
	}
	
	public DepositData(LocalDateTime time, CurrencyAmountData currencyAmount) {
		super();
		this.time = time;
		this.currencyAmount = currencyAmount;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public CurrencyAmountData getCurrencyAmount() {
		return currencyAmount;
	}
	
	public void setCurrencyAmount(CurrencyAmountData currencyAmount) {
		this.currencyAmount = currencyAmount;
	}
	
	
}
