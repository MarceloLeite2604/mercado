package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "ORDERS")
@NamedQuery(name = "OrderPO.findAll", query = "SELECT o FROM OrderPO o")
public class OrderPO {

	@EmbeddedId
	private OrderIdPO orderIdPO;
	
	@Column(name="FIRST_CURRENCY")
	private Currency firstCurrency;
	
	@Column(name="SECOND_CURRENCY")
	private Currency secondCurrency;
	
	@Column(name="HAS_FILLS")
	private Boolean hasFills;
	
	@Column(name="QUANTITY")
	private BigDecimal quantity;
	
	@Column(name="LIMIT_PRICE")
	private BigDecimal limitPrice;

	@Column(name="EXECUTED_QUANTITY")
	private BigDecimal executedQuantity;

	@Column(name="EXECUTED_PRICE_AVERAGE")
	private BigDecimal executedPriceAverage;

	@Column(name="FEE")
	private BigDecimal fee;

	@Column(name="CREATED")
	private ZonedDateTime created;

	@Column(name="UPDATED")
	private ZonedDateTime updated;
	
	// bi-directional many-to-one association to StrategyPO
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	List<OperationPO> operationPOs;

	public OrderIdPO getOrderIdPO() {
		return orderIdPO;
	}

	public void setOrderIdPO(OrderIdPO orderIdPO) {
		this.orderIdPO = orderIdPO;
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

	public ZonedDateTime getCreatedTimestamp() {
		return created;
	}

	public void setCreatedTimestamp(ZonedDateTime created) {
		this.created = created;
	}

	public ZonedDateTime getUpdatedTimestamp() {
		return updated;
	}

	public void setUpdatedTimestamp(ZonedDateTime updated) {
		this.updated = updated;
	}

	// private List<Operation> operations;
	
	
}
