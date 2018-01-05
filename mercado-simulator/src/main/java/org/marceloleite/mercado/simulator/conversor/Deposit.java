package org.marceloleite.mercado.simulator.conversor;

import java.time.LocalDateTime;

import org.marceloleite.mercado.simulator.AbstractTimedObject;
import org.marceloleite.mercado.simulator.structure.CurrencyAmountData;
import org.marceloleite.mercado.simulator.structure.DepositData;

public class Deposit extends AbstractTimedObject {

	private LocalDateTime time;

	private CurrencyAmountData currencyAmount;

	public Deposit(DepositData depositData) {
		this.time = depositData.getTime();
		this.currencyAmount = depositData.getCurrencyAmount();
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
