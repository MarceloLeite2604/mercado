package org.marceloleite.mercado.base.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import org.marceloleite.mercado.base.model.temporalcontroller.AbstractTimedObject;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.converter.ZonedDateTimeToStringConverter;
import org.marceloleite.mercado.data.OperationData;
import org.marceloleite.mercado.data.OrderData;

public class Order extends AbstractTimedObject {

	private Account account;

	private Long id;

	private Currency firstCurrency;

	private Currency secondCurrency;

	private OrderType type;

	private OrderStatus status;

	private Boolean hasFills;

	private BigDecimal quantity;

	private BigDecimal limitPrice;

	private BigDecimal executedQuantity;

	private BigDecimal executedPriceAverage;

	private BigDecimal fee;
	
	private ZonedDateTime intended;

	private ZonedDateTime created;

	private ZonedDateTime updated;

	List<OperationData> operationDatas;

	public Order() {
		super();
	}
	
	public Order(Currency firstCurrency, Currency secondCurrency, OrderType type, BigDecimal quantity, BigDecimal limitPrice,
			ZonedDateTime intended) {
		super();
		this.firstCurrency = firstCurrency;
		this.secondCurrency = secondCurrency;
		this.type = type;
		this.quantity = quantity;
		this.limitPrice = limitPrice;
		this.intended = intended;
		this.status = OrderStatus.UNDEFINED;
	}



	public Order(OrderData orderData) {
		super();
		this.id = orderData.getId();
		this.firstCurrency = orderData.getFirstCurrency();
		this.secondCurrency = orderData.getSecondCurrency();
		this.type = orderData.getType();
		this.status = orderData.getStatus();
		this.hasFills = orderData.getHasFills();
		this.quantity = orderData.getQuantity();
		this.limitPrice = orderData.getLimitPrice();
		this.executedQuantity = orderData.getExecutedQuantity();
		this.executedPriceAverage = orderData.getExecutedPriceAverage();
		this.fee = orderData.getFee();
		this.created = orderData.getCreated();
		this.updated = orderData.getUpdated();
		this.operationDatas = orderData.getOperationDatas();
	}
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
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

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(BigDecimal limitPrice) {
		this.limitPrice = limitPrice;
	}

	public BigDecimal getExecutedQuantity() {
		return executedQuantity;
	}

	public void setExecutedQuantity(BigDecimal executedQuantity) {
		this.executedQuantity = executedQuantity;
	}

	public BigDecimal getExecutedPriceAverage() {
		return executedPriceAverage;
	}

	public void setExecutedPriceAverage(BigDecimal executedPriceAverage) {
		this.executedPriceAverage = executedPriceAverage;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
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

	public List<OperationData> getOperationDatas() {
		return operationDatas;
	}

	public void setOperationDatas(List<OperationData> operationDatas) {
		this.operationDatas = operationDatas;
	}
	
	@Override
	public String toString() {
		String result = null;
		switch (type) {
		case BUY:
			result = toStringBuyOrder();
			break;
		case SELL:
			result = toStringSellOrder();
			break;
		}
		return result;
	}
	
	private String toStringBuyOrder() {
		StringBuffer stringBuffer = new StringBuffer();
		CurrencyAmount currencyAmountToBuy = new CurrencyAmount(secondCurrency, quantity);
		CurrencyAmount currencyAmountToPay = new CurrencyAmount(firstCurrency, limitPrice);
		
		stringBuffer.append("buying ");
		stringBuffer.append(currencyAmountToBuy + " ");
		stringBuffer.append("paying a unit price of ");
		stringBuffer.append(currencyAmountToPay + " ");
		stringBuffer.append("at " + new ZonedDateTimeToStringConverter().convertTo(intended));
		
		return stringBuffer.toString();
	}
	
	private String toStringSellOrder() {
		StringBuffer stringBuffer = new StringBuffer();
		CurrencyAmount currencyAmountToSell = new CurrencyAmount(secondCurrency, quantity);
		CurrencyAmount currencyAmountToReceive = new CurrencyAmount(firstCurrency, limitPrice);
		
		stringBuffer.append("selling ");
		stringBuffer.append(currencyAmountToSell + " ");
		stringBuffer.append("receiving a unit price of ");
		stringBuffer.append(currencyAmountToReceive + " ");
		stringBuffer.append("at " + new ZonedDateTimeToStringConverter().convertTo(intended));
		
		return stringBuffer.toString();
	}



	@Override
	public ZonedDateTime getTime() {
		return intended;
	}

}
