package org.marceloleite.mercado.api.negotiation.methods.getaccountinfo.response;

import java.math.BigDecimal;

public class CurrencyInfo {

	private BigDecimal available;

	private BigDecimal total;

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
