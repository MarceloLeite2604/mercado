package org.marceloleite.mercado.data;

import java.time.ZonedDateTime;

public class OperationData {

	private OrderData orderData;

	private Long id;

	private Double quantity;

	private Double price;

	private Double feeRate;

	private ZonedDateTime executed;

	public OperationData() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OrderData getOrderData() {
		return orderData;
	}

	public void setOrderData(OrderData orderData) {
		this.orderData = orderData;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(Double feeRate) {
		this.feeRate = feeRate;
	}

	public ZonedDateTime getExecuted() {
		return executed;
	}

	public void setExecuted(ZonedDateTime executed) {
		this.executed = executed;
	}
}
