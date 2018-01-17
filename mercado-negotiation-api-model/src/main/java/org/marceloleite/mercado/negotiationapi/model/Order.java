package org.marceloleite.mercado.negotiationapi.model;

import java.time.LocalDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.negotiationapi.model.listorders.Operation;

public class Order {

	private long id;

	private CurrencyPair currencyPair;

	private OrderType orderType;

	private OrderStatus orderStatus;

	private boolean hasFills;

	private double quantity;

	private double limitPrice;

	private double executedQuantity;

	private double executedPriceAverage;

	private double fee;

	private LocalDateTime createdTimestamp;

	private LocalDateTime updatedTimestamp;

	private List<Operation> operations;

	public Order(long id, CurrencyPair currencyPair, OrderType orderType, OrderStatus orderStatus, boolean hasFills,
			double quantity, double limitPrice, double executedQuantity, double executedPriceAverage, double fee,
			LocalDateTime createdTimestamp, LocalDateTime updatedTimestamp, List<Operation> operations) {
		super();
		this.id = id;
		this.currencyPair = currencyPair;
		this.orderType = orderType;
		this.orderStatus = orderStatus;
		this.hasFills = hasFills;
		this.quantity = quantity;
		this.limitPrice = limitPrice;
		this.executedQuantity = executedQuantity;
		this.executedPriceAverage = executedPriceAverage;
		this.fee = fee;
		this.createdTimestamp = createdTimestamp;
		this.updatedTimestamp = updatedTimestamp;
		this.operations = operations;
	}

	public Order() {
		this(0l, null, null, null, false, 0.0, 0.0, 0.0, 0.0, 0.0, null, null, null);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CurrencyPair getCurrencyPair() {
		return currencyPair;
	}

	public void setCurrencyPair(CurrencyPair currencyPair) {
		this.currencyPair = currencyPair;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public boolean isHasFills() {
		return hasFills;
	}

	public void setHasFills(boolean hasFills) {
		this.hasFills = hasFills;
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

	public double getExecutedQuantity() {
		return executedQuantity;
	}

	public void setExecutedQuantity(double executedQuantity) {
		this.executedQuantity = executedQuantity;
	}

	public double getExecutedPriceAverage() {
		return executedPriceAverage;
	}

	public void setExecutedPriceAverage(double executedPriceAverage) {
		this.executedPriceAverage = executedPriceAverage;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public LocalDateTime getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public LocalDateTime getUpdatedTimestamp() {
		return updatedTimestamp;
	}

	public void setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
		this.updatedTimestamp = updatedTimestamp;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}
}
