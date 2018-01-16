package org.marceloleite.mercado.negotiationapi.model.listorderbook;

public class OrderbookRegister {

	private long id;

	private double quantity;

	private double limitPrice;

	private boolean owner;

	public OrderbookRegister(long id, double quantity, double limitPrice, boolean owner) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.limitPrice = limitPrice;
		this.owner = owner;
	}

	public OrderbookRegister() {
		this(0l, 0.0, 0.0, false);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(double limitPrice) {
		this.limitPrice = limitPrice;
	}

	public boolean isOwner() {
		return owner;
	}

	public void setOwner(boolean owner) {
		this.owner = owner;
	}
}
