package org.marceloleite.mercado.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.marceloleite.mercado.attributeconverter.ZonedDateTimeAttributeConverter;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.commons.OrderType;
import org.marceloleite.mercado.commons.deserializer.OrderStatusDeserializer;
import org.marceloleite.mercado.commons.serializer.OrderStatusSerializer;
import org.marceloleite.mercado.xmladapter.CurrencyXmlAdapter;
import org.marceloleite.mercado.xmladapter.OrderStatusXmlAdapter;
import org.marceloleite.mercado.xmladapter.OrderTypeXmlAdapter;
import org.marceloleite.mercado.xmladapter.ZonedDateTimeXmlAdapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "ORDERS")
@JsonIgnoreProperties({ "id", "account" })
@JsonPropertyOrder({ "firstCurrency", "secondCurrency", "type", "status", "hasFills", "quantity", "limitPrice",
		"executedQuantity", "executedPriceAverage", "fee", "created", "updated", "intended", "operations" })
@XmlRootElement(name = "order")
@XmlType(propOrder = { "firstCurrency", "secondCurrency", "type", "status", "hasFills", "quantity", "limitPrice",
		"executedQuantity", "executedPriceAverage", "fee", "created", "updated", "intended", "operations" })
public class Order {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ACCO_ID", nullable = false, foreignKey = @ForeignKey(name = "ORDE_ACCO_FK"))
	private Account account;

	@Column(name = "FIRST_CURRENCY", nullable = false, length = 4)
	private Currency firstCurrency;

	@Column(name = "SECOND_CURRENCY", nullable = false, length = 4)
	private Currency secondCurrency;

	@Column(name = "TYPE", nullable = false, length = 4)
	private OrderType type;

	@Column(name = "STATUS", nullable = false, length = 16)
	@JsonSerialize(using=OrderStatusSerializer.class)
	@JsonDeserialize(using=OrderStatusDeserializer.class)
	private OrderStatus status;

	@Column(name = "HAS_FILLS", nullable = false, length = 8)
	private Boolean hasFills;

	@Column(name = "QUANTITY", nullable = false)
	private BigDecimal quantity;

	@Column(name = "LIMIT_PRICE", nullable = false)
	private BigDecimal limitPrice;

	@Column(name = "EXECUTED_QUANTITY", nullable = false)
	private BigDecimal executedQuantity;

	@Column(name = "EXECUTED_PRICE_AVERAGE", nullable = false)
	private BigDecimal executedPriceAverage;

	@Column(name = "FEE", nullable = false)
	private BigDecimal fee;

	@Column(name = "CREATED", nullable = true)
	@Convert(converter = ZonedDateTimeAttributeConverter.class)
	private ZonedDateTime created;

	@Column(name = "UPDATED", nullable = true)
	@Convert(converter = ZonedDateTimeAttributeConverter.class)
	private ZonedDateTime updated;

	@Column(name = "INTENDED", nullable = false)
	@Convert(converter = ZonedDateTimeAttributeConverter.class)
	private ZonedDateTime intended;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<Operation> operations;

	@XmlTransient
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlTransient
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@XmlElement(name = "firstCurrency")
	@XmlJavaTypeAdapter(CurrencyXmlAdapter.class)
	public Currency getFirstCurrency() {
		return firstCurrency;
	}

	public void setFirstCurrency(Currency firstCurrency) {
		this.firstCurrency = firstCurrency;
	}

	@XmlElement(name = "secondCurrency")
	@XmlJavaTypeAdapter(CurrencyXmlAdapter.class)
	public Currency getSecondCurrency() {
		return secondCurrency;
	}

	public void setSecondCurrency(Currency secondCurrency) {
		this.secondCurrency = secondCurrency;
	}

	@XmlElement(name = "type")
	@XmlJavaTypeAdapter(OrderTypeXmlAdapter.class)
	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}

	@XmlElement(name = "status")
	@XmlJavaTypeAdapter(OrderStatusXmlAdapter.class)
	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	@XmlElement(name = "hasFills")
	public Boolean getHasFills() {
		return hasFills;
	}

	public void setHasFills(Boolean hasFills) {
		this.hasFills = hasFills;
	}

	@XmlElement(name = "quantity")
	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	@XmlElement(name = "limitPrice")
	public BigDecimal getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(BigDecimal limitPrice) {
		this.limitPrice = limitPrice;
	}

	@XmlElement(name = "executedQuantity")
	public BigDecimal getExecutedQuantity() {
		return executedQuantity;
	}

	public void setExecutedQuantity(BigDecimal executedQuantity) {
		this.executedQuantity = executedQuantity;
	}

	@XmlElement(name = "executedPriceAverage")
	public BigDecimal getExecutedPriceAverage() {
		return executedPriceAverage;
	}

	public void setExecutedPriceAverage(BigDecimal executedPriceAverage) {
		this.executedPriceAverage = executedPriceAverage;
	}

	@XmlElement(name = "fee")
	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	@XmlElement(name = "created")
	@XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
	public ZonedDateTime getCreated() {
		return created;
	}

	public void setCreated(ZonedDateTime created) {
		this.created = created;
	}

	@XmlElement(name = "updated")
	@XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
	public ZonedDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(ZonedDateTime updated) {
		this.updated = updated;
	}

	@XmlElement(name = "intended")
	@XmlJavaTypeAdapter(ZonedDateTimeXmlAdapter.class)
	public ZonedDateTime getIntended() {
		return intended;
	}

	public void setIntended(ZonedDateTime intended) {
		this.intended = intended;
	}

	@XmlElementWrapper(name = "operations")
	@XmlElement(name = "operation")
	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}

	public void addOperation(Operation operation) {
		operation.setOrder(this);
		getOperations().add(operation);
	}
}
