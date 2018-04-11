package org.marceloleite.mercado.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.commons.OrderType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@Table(name = "ORDERS")
@JsonIgnoreProperties({ "id", "account" })
@JsonPropertyOrder({ "firstCurrency", "secondCurrency", "type", "status", "hasFills", "quantity", "limitPrice",
		"executedQuantity", "executedPriceAverage", "fee", "created", "updated", "intended" })
@XmlRootElement(name = "order")
@XmlType(propOrder = { "firstCurrency", "secondCurrency", "type", "status", "hasFills", "quantity", "limitPrice",
		"executedQuantity", "executedPriceAverage", "fee", "created", "updated", "intended" })
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
	private OrderStatus status;

	@Column(name = "HAS_FILLS", nullable = false)
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
	private ZonedDateTime created;

	@Column(name = "UPDATED", nullable = true)
	private ZonedDateTime updated;

	@Column(name = "INTENDED", nullable = false)
	private ZonedDateTime intended;

	// bi-directional many-to-one association to StrategyPO
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<Operation> operations;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
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

	public ZonedDateTime getIntended() {
		return intended;
	}

	public void setIntended(ZonedDateTime intended) {
		this.intended = intended;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}
}
