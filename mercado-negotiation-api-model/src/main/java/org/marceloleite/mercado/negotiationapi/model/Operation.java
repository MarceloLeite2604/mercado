package org.marceloleite.mercado.negotiationapi.model;

import java.time.LocalDateTime;

public class Operation {

	private long id;

	private double quantity;

	private double price;

	private double feeRate;

	private LocalDateTime executedTimestamp;

	public Operation(long id, double quantity, double price, double feeRate, LocalDateTime executedTimestamp) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.price = price;
		this.feeRate = feeRate;
		this.executedTimestamp = executedTimestamp;
	}

	public Operation() {
		this(0, 0.0d, 0.0d, 0.0d, null);
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(double feeRate) {
		this.feeRate = feeRate;
	}

	public LocalDateTime getExecutedTimestamp() {
		return executedTimestamp;
	}

	public void setExecutedTimestamp(LocalDateTime executedTimestamp) {
		this.executedTimestamp = executedTimestamp;
	}
}
