package org.marceloleite.mercado.data;

import java.time.ZonedDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.commons.OrderType;

public class OrderData {

	private AccountData accountData;

	private Long id;

	private Currency firstCurrency;

	private Currency secondCurrency;

	private OrderType type;

	private OrderStatus status;

	private Boolean hasFills;

	private Double quantity;

	private Double limitPrice;

	private Double executedQuantity;

	private Double executedPriceAverage;

	private Double fee;

	private ZonedDateTime created;

	private ZonedDateTime updated;

	private ZonedDateTime intended;

	List<OperationData> operationDatas;

	public OrderData() {
		super();
	}

	public AccountData getAccountData() {
		return accountData;
	}

	public void setAccountData(AccountData accountData) {
		this.accountData = accountData;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Currency getFirstCurrency() {
		return firstCurrency;
	}

	public void setFirstCurrency(Currency firstCurrency) {
		this.firstCurrency = firstCurrency;
	}

	public Currency getSecondCurrency() {
		return secondCurrency;
	}

	public void setSecondCurrency(Currency secondCurrency) {
		this.secondCurrency = secondCurrency;
	}

	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Boolean getHasFills() {
		return hasFills;
	}

	public void setHasFills(Boolean hasFills) {
		this.hasFills = hasFills;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(Double limitPrice) {
		this.limitPrice = limitPrice;
	}

	public Double getExecutedQuantity() {
		return executedQuantity;
	}

	public void setExecutedQuantity(Double executedQuantity) {
		this.executedQuantity = executedQuantity;
	}

	public Double getExecutedPriceAverage() {
		return executedPriceAverage;
	}

	public void setExecutedPriceAverage(Double executedPriceAverage) {
		this.executedPriceAverage = executedPriceAverage;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public ZonedDateTime getCreated() {
		return created;
	}

	public void setCreated(ZonedDateTime created) {
		this.created = created;
	}

	public ZonedDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(ZonedDateTime updated) {
		this.updated = updated;
	}

	public ZonedDateTime getIntended() {
		return intended;
	}

	public void setIntended(ZonedDateTime intended) {
		this.intended = intended;
	}

	public List<OperationData> getOperationDatas() {
		return operationDatas;
	}

	public void setOperationDatas(List<OperationData> operationDatas) {
		this.operationDatas = operationDatas;
	}

}