package org.marceloleite.mercado.xml.structures;

import java.time.ZonedDateTime;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.xml.adapters.CurrencyXmlAdapter;
import org.marceloleite.mercado.xml.adapters.OrderStatusXmlAdapter;
import org.marceloleite.mercado.xml.adapters.OrderTypeXmlAdapter;
import org.marceloleite.mercado.xml.adapters.ZonedDateTimeXmlAdapter;

@XmlRootElement(name="order")
public class XmlOrder {

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

	List<XmlOperation> xmlOperations;
	
	

	public XmlOrder(Long id, Currency firstCurrency, Currency secondCurrency, OrderType type, OrderStatus status,
			Boolean hasFills, Double quantity, Double limitPrice, Double executedQuantity, Double executedPriceAverage,
			Double fee, ZonedDateTime created, ZonedDateTime updated, ZonedDateTime intended,
			List<XmlOperation> xmlOperations) {
		super();
		this.id = id;
		this.firstCurrency = firstCurrency;
		this.secondCurrency = secondCurrency;
		this.type = type;
		this.status = status;
		this.hasFills = hasFills;
		this.quantity = quantity;
		this.limitPrice = limitPrice;
		this.executedQuantity = executedQuantity;
		this.executedPriceAverage = executedPriceAverage;
		this.fee = fee;
		this.created = created;
		this.updated = updated;
		this.intended = intended;
		this.xmlOperations = xmlOperations;
	}

	public XmlOrder() {
		this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	}
	
	public XmlOrder(Currency firstCurrency, Currency secondCurrency, Double quantity, Double limitPrice) {
		super();
		this.firstCurrency = firstCurrency;
		this.secondCurrency = secondCurrency;
		this.quantity = quantity;
		this.limitPrice = limitPrice;
	}

	@XmlElement
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlJavaTypeAdapter(CurrencyXmlAdapter.class)
	@XmlElement
	public Currency getFirstCurrency() {
		return firstCurrency;
	}

	public void setFirstCurrency(Currency firstCurrency) {
		this.firstCurrency = firstCurrency;
	}

	@XmlJavaTypeAdapter(CurrencyXmlAdapter.class)
	@XmlElement
	public Currency getSecondCurrency() {
		return secondCurrency;
	}

	public void setSecondCurrency(Currency secondCurrency) {
		this.secondCurrency = secondCurrency;
	}

	@XmlJavaTypeAdapter(OrderTypeXmlAdapter.class)
	@XmlElement
	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}

	@XmlJavaTypeAdapter(OrderStatusXmlAdapter.class)
	@XmlElement
	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	@XmlElement
	public Boolean getHasFills() {
		return hasFills;
	}

	public void setHasFills(Boolean hasFills) {
		this.hasFills = hasFills;
	}

	@XmlElement
	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@XmlElement
	public Double getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(Double limitPrice) {
		this.limitPrice = limitPrice;
	}

	@XmlElement
	public Double getExecutedQuantity() {
		return executedQuantity;
	}

	public void setExecutedQuantity(Double executedQuantity) {
		this.executedQuantity = executedQuantity;
	}

	@XmlElement
	public Double getExecutedPriceAverage() {
		return executedPriceAverage;
	}

	public void setExecutedPriceAverage(Double executedPriceAverage) {
		this.executedPriceAverage = executedPriceAverage;
	}

	@XmlElement
	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	@XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
	@XmlElement
	public ZonedDateTime getCreated() {
		return created;
	}

	public void setCreated(ZonedDateTime created) {
		this.created = created;
	}

	@XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
	@XmlElement
	public ZonedDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(ZonedDateTime updated) {
		this.updated = updated;
	}

	@XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
	@XmlElement
	public ZonedDateTime getIntended() {
		return intended;
	}

	public void setIntended(ZonedDateTime intended) {
		this.intended = intended;
	}

	@XmlElement
	@XmlElementWrapper(name="operations")
	public List<XmlOperation> getXmlOperations() {
		return xmlOperations;
	}

	public void setXmlOperations(List<XmlOperation> operationDatas) {
		this.xmlOperations = operationDatas;
	}
}
