package org.marceloleite.mercado.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.marceloleite.mercado.database.attributeconverter.ZonedDateTimeAttributeConverter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@Table(name = "OPERATIONS")
@JsonIgnoreProperties({ "id", "order" })
@JsonPropertyOrder({ "quantity", "price", "feeRate", "executed" })
@XmlRootElement(name = "order")
// @XmlType(propOrder = { "quantity", "price", "feeRate", "executed" })
public class Operation {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ORDE_ID", nullable = false, foreignKey = @ForeignKey(name = "OPER_ORDE_FK"))
	private Order order;

	@Column(name = "QUANTITY")
	private BigDecimal quantity;

	@Column(name = "PRICE")
	private BigDecimal price;

	@Column(name = "FEE_RATE")
	private BigDecimal feeRate;

	@Column(name = "EXECUTED")
	private ZonedDateTime executed;

	@XmlTransient
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlTransient
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@XmlElement
	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	@XmlElement
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@XmlElement
	public BigDecimal getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(BigDecimal feeRate) {
		this.feeRate = feeRate;
	}

	@XmlElement
	@Convert(converter = ZonedDateTimeAttributeConverter.class)
	public ZonedDateTime getExecuted() {
		return executed;
	}

	public void setExecuted(ZonedDateTime executed) {
		this.executed = executed;
	}
}
