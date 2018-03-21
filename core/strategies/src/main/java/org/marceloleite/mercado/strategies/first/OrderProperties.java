package org.marceloleite.mercado.strategies.first;

import org.marceloleite.mercado.base.model.CurrencyAmount;
import org.marceloleite.mercado.commons.OrderType;

public class OrderProperties {

	private OrderType orderType;

	private CurrencyAmount first;

	private CurrencyAmount second;

	private CurrencyAmount unitPrice;

	private boolean secondAdjusted;

	private boolean firstAdjusted;

	private boolean cancelled;
	
	public OrderProperties(OrderType orderType) {
		this.firstAdjusted = false;
		this.secondAdjusted = false;
		this.cancelled = false;
		this.orderType = orderType;
	}

	public CurrencyAmount getFirst() {
		return first;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public void setFirst(CurrencyAmount first) {
		this.first = first;
	}

	public CurrencyAmount getSecond() {
		return second;
	}

	public void setSecond(CurrencyAmount second) {
		this.second = second;
	}

	public CurrencyAmount getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(CurrencyAmount unitPrice) {
		this.unitPrice = unitPrice;
	}

	public boolean isSecondAdjusted() {
		return secondAdjusted;
	}

	public void setSecondAdjusted(boolean secondAdjusted) {
		this.secondAdjusted = secondAdjusted;
	}

	public boolean isFirstAdjusted() {
		return firstAdjusted;
	}

	public void setFirstAdjusted(boolean firstAdjusted) {
		this.firstAdjusted = firstAdjusted;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}