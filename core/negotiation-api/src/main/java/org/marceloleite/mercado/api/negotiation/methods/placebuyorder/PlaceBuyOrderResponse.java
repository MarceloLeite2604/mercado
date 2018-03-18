package org.marceloleite.mercado.api.negotiation.methods.placebuyorder;

import org.marceloleite.mercado.data.OrderData;

public class PlaceBuyOrderResponse {

	private OrderData orderData;

	public PlaceBuyOrderResponse(OrderData orderData) {
		super();
		this.orderData = orderData;
	}

	public PlaceBuyOrderResponse() {
		this(null);
	}

	public OrderData getOrderData() {
		return orderData;
	}

	public void setOrder(OrderData orderData) {
		this.orderData = orderData;
	}
}
