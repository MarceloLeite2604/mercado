package org.marceloleite.mercado.databaseretriever.persistence.objects;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
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
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.OrderStatus;
import org.marceloleite.mercado.commons.OrderType;

@Entity
@Table(name = "ORDERS")
@NamedQuery(name = "OrderPO.findAll", query = "SELECT o FROM OrderPO o")
public class OrderPO {

	@EmbeddedId
	private OrderIdPO id;

	@Column(name = "FIRST_CURRENCY")
	private Currency firstCurrency;

	@Column(name = "SECOND_CURRENCY")
	private Currency secondCurrency;

	@Column(name = "TYPE")
	private OrderType type;

	@Column(name = "STATUS")
	private OrderStatus status;

	@Column(name = "HAS_FILLS")
	private Boolean hasFills;

	@Column(name = "QUANTITY")
	private BigDecimal quantity;

	@Column(name = "LIMIT_PRICE")
	private BigDecimal limitPrice;

	@Column(name = "EXECUTED_QUANTITY")
	private BigDecimal executedQuantity;

	@Column(name = "EXECUTED_PRICE_AVERAGE")
	private BigDecimal executedPriceAverage;

	@Column(name = "FEE")
	private BigDecimal fee;

	@Column(name = "CREATED")
	private ZonedDateTime created;

	@Column(name = "UPDATED")
	private ZonedDateTime updated;
	
	@Column(name= "INTENDED")
	private ZonedDateTime intended;

	// bi-directional many-to-one association to StrategyPO
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	List<OperationPO> operationPOs;

	public OrderIdPO getId() {
		return id;
	}

	public void setId(OrderIdPO id) {
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

	public List<OperationPO> getOperationPOs() {
		return operationPOs;
	}

	public void setOperationPOs(List<OperationPO> operationPOs) {
		this.operationPOs = operationPOs;
	}

}
