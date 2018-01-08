package org.marceloleite.mercado.simulator;

import java.time.LocalDateTime;

import org.marceloleite.mercado.simulator.structure.DepositData;
import org.marceloleite.mercado.simulator.temporalcontroller.AbstractTimedObject;

public class Deposit extends AbstractTimedObject {

	private LocalDateTime time;

	private CurrencyAmount currencyAmount;

	public Deposit(DepositData depositData) {
		this.time = depositData.getTime();
		this.currencyAmount = new CurrencyAmount(depositData.getCurrencyAmount());
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
