package org.marceloleite.mercado.simulator.conversor;

import java.time.LocalDateTime;

public class OldDeposit {
	
	private LocalDateTime time;
	
	private CurrencyAmount currencyAmount;
	
	public OldDeposit() {
		super();
	}
	
	public OldDeposit(LocalDateTime time, CurrencyAmount currencyAmount) {
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

	public CurrencyAmount getCurrencyAmount() {
		return currencyAmount;
	}
	
	public void setCurrencyAmount(CurrencyAmount currencyAmount) {
		this.currencyAmount = currencyAmount;
	}
	
	
}
