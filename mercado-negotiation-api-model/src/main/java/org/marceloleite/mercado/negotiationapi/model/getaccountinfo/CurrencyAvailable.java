package org.marceloleite.mercado.negotiationapi.model.getaccountinfo;

public class CurrencyAvailable {

	private double available;

	private double total;

	private long amountOpenOrders;

	public CurrencyAvailable(double available, double total, long amountOpenOrders) {
		super();
		this.available = available;
		this.total = total;
		this.amountOpenOrders = amountOpenOrders;
	}

	public CurrencyAvailable() {
		this(0.0, 0.0, 0l);
	}

	public double getAvailable() {
		return available;
	}

	public void setAvailable(double available) {
		this.available = available;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public long getAmountOpenOrders() {
		return amountOpenOrders;
	}

	public void setAmountOpenOrders(long amountOpenOrders) {
		this.amountOpenOrders = amountOpenOrders;
	}
}
