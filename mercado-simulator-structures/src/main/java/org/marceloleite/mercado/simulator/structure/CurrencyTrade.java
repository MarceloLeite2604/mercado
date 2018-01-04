package org.marceloleite.mercado.simulator.structure;

public class CurrencyTrade {

	private double price;

	private CurrencyAmount from;

	private CurrencyAmount to;

	private CurrencyAmount comission;

	public CurrencyTrade() {
		super();
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public CurrencyAmount getFrom() {
		return from;
	}

	public void setFrom(CurrencyAmount from) {
		this.from = from;
	}

	public CurrencyAmount getTo() {
		return to;
	}

	public void setTo(CurrencyAmount to) {
		this.to = to;
	}

	public CurrencyAmount getComission() {
		return comission;
	}

	public void setComission(CurrencyAmount comission) {
		this.comission = comission;
	}
}
