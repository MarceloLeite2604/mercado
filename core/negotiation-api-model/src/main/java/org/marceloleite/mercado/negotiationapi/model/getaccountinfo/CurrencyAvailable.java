package org.marceloleite.mercado.negotiationapi.model.getaccountinfo;

import org.marceloleite.mercado.commons.MercadoBigDecimal;

public class CurrencyAvailable {

	private MercadoBigDecimal available;

	private MercadoBigDecimal total;

	private long amountOpenOrders;

	public CurrencyAvailable(MercadoBigDecimal available, MercadoBigDecimal total, long amountOpenOrders) {
		super();
		this.available = available;
		this.total = total;
		this.amountOpenOrders = amountOpenOrders;
	}

	public CurrencyAvailable() {
		this(new MercadoBigDecimal("0.0"), new MercadoBigDecimal("0.0"), 0l);
	}

	public MercadoBigDecimal getAvailable() {
		return available;
	}

	public void setAvailable(MercadoBigDecimal available) {
		this.available = available;
	}

	public MercadoBigDecimal getTotal() {
		return total;
	}

	public void setTotal(MercadoBigDecimal total) {
		this.total = total;
	}

	public long getAmountOpenOrders() {
		return amountOpenOrders;
	}

	public void setAmountOpenOrders(long amountOpenOrders) {
		this.amountOpenOrders = amountOpenOrders;
	}
}
