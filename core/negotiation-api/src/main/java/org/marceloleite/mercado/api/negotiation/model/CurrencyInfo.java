package org.marceloleite.mercado.api.negotiation.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyInfo {

	private BigDecimal available;

	private BigDecimal total;

	@JsonProperty("amount_open_orders")
	private Long amountOpenOrders;

	public BigDecimal getAvailable() {
		return available;
	}

	public void setAvailable(BigDecimal available) {
		this.available = available;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Long getAmountOpenOrders() {
		return amountOpenOrders;
	}

	public void setAmountOpenOrders(Long amountOpenOrders) {
		this.amountOpenOrders = amountOpenOrders;
	}
}
