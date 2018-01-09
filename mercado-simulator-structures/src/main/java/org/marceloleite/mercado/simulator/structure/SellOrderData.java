package org.marceloleite.mercado.simulator.structure;

import java.time.LocalDateTime;

public class SellOrderData {

	private LocalDateTime time;

	private CurrencyAmountData currencyAmountToSell;

	private CurrencyAmountData currencyAmountToReceive;

	public SellOrderData() {
		this(null, null, null);
	}

	public SellOrderData(LocalDateTime time, CurrencyAmountData currencyAmountToSell, CurrencyAmountData currencyAmountToReceive) {
		super();
		this.time = time;
		this.currencyAmountToSell = currencyAmountToSell;
		this.currencyAmountToReceive = currencyAmountToReceive;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public CurrencyAmountData getCurrencyAmountToSell() {
		return currencyAmountToSell;
	}

	public void setCurrencyAmountToSell(CurrencyAmountData currencyAmountToSell) {
		this.currencyAmountToSell = currencyAmountToSell;
	}

	public CurrencyAmountData getCurrencyAmountToReceive() {
		return currencyAmountToReceive;
	}

	public void setCurrencyAmountToReceive(CurrencyAmountData currencyAmountToReceive) {
		this.currencyAmountToReceive = currencyAmountToReceive;
	}
}
