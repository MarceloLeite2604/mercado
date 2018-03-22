package org.marceloleite.mercado.negotiationapi.model.getaccountinfo;

import java.math.BigDecimal;

public class CurrencyAvailable {

	private BigDecimal available;

	private BigDecimal total;

	private long amountOpenOrders;

	public CurrencyAvailable(BigDecimal available, BigDecimal total, long amountOpenOrders) {
		super();
		this.available = available;
		this.total = total;
		this.amountOpenOrders = amountOpenOrders;
	}

	public CurrencyAvailable() {
		this(new BigDecimal("0.0"), new BigDecimal("0.0"), 0l);
	}

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

	public long getAmountOpenOrders() {
		return amountOpenOrders;
	}

	public void setAmountOpenOrders(long amountOpenOrders) {
		this.amountOpenOrders = amountOpenOrders;
	}
}
