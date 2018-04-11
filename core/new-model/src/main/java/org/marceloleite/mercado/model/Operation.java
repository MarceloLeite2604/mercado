package org.marceloleite.mercado.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@Table(name = "ORDERS")
@JsonIgnoreProperties({ "id", "order" })
@JsonPropertyOrder({ "quantity", "price", "feeRate", "executed" })
@XmlRootElement(name = "order")
@XmlType(propOrder = { "firstCurrency", "secondCurrency", "type", "status", "hasFills", "quantity", "limitPrice",
		"executedQuantity", "executedPriceAverage", "fee", "created", "updated", "intended" })
public class Operation {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ACCO_ID", nullable = false, foreignKey = @ForeignKey(name = "OPER_ORDE_FK"))
	private Order order;

	@Column(name = "QUANTITY")
	private BigDecimal quantity;

	@Column(name = "PRICE")
	private BigDecimal price;

	@Column(name = "FEE_RATE")
	private BigDecimal feeRate;

	@Column(name = "EXECUTED")
	private ZonedDateTime executed;

	public OperationIdPO getId() {
		return id;
	}

	public void setId(OperationIdPO id) {
		this.id = id;
	}

	public OrderPO getOrder() {
		return order;
	}

	public void setOrder(OrderPO order) {
		this.order = order;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(BigDecimal feeRate) {
		this.feeRate = feeRate;
	}

	public ZonedDateTime getExecuted() {
		return executed;
	}

	public void setExecuted(ZonedDateTime executed) {
		this.executed = executed;
	}
}
