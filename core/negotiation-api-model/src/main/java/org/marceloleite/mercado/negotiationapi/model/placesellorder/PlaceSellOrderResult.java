package org.marceloleite.mercado.negotiationapi.model.placesellorder;

import org.marceloleite.mercado.data.OrderData;

public class PlaceSellOrderResult {

	private OrderData orderData;

	public PlaceSellOrderResult(OrderData orderData) {
		super();
		this.orderData = orderData;
	}

	public PlaceSellOrderResult() {
		this(null);
	}

	public OrderData getOrderData() {
		return orderData;
	}

	public void setOrder(OrderData orderData) {
		this.orderData = orderData;
	}
}
