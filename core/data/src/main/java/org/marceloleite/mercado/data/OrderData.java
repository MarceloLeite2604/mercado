package org.marceloleite.mercado.data;

import java.time.ZonedDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.MercadoBigDecimal;
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

	private MercadoBigDecimal quantity;

	private MercadoBigDecimal limitPrice;

	private MercadoBigDecimal executedQuantity;

	private MercadoBigDecimal executedPriceAverage;

	private MercadoBigDecimal fee;

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

	public MercadoBigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(MercadoBigDecimal quantity) {
		this.quantity = quantity;
	}

	public MercadoBigDecimal getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(MercadoBigDecimal limitPrice) {
		this.limitPrice = limitPrice;
	}

	public MercadoBigDecimal getExecutedQuantity() {
		return executedQuantity;
	}

	public void setExecutedQuantity(MercadoBigDecimal executedQuantity) {
		this.executedQuantity = executedQuantity;
	}

	public MercadoBigDecimal getExecutedPriceAverage() {
		return executedPriceAverage;
	}

	public void setExecutedPriceAverage(MercadoBigDecimal executedPriceAverage) {
		this.executedPriceAverage = executedPriceAverage;
	}

	public MercadoBigDecimal getFee() {
		return fee;
	}

	public void setFee(MercadoBigDecimal fee) {
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
