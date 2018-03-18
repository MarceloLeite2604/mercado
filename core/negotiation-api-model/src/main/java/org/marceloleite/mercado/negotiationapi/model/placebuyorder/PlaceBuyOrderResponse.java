package org.marceloleite.mercado.negotiationapi.model.placebuyorder;

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

	public OrderData getOrder() {
		return orderData;
	}

	public void setOrder(OrderData orderData) {
		this.orderData = orderData;
	}
}
