package org.marceloleite.mercado.negotiationapi.model.getorder;

import org.marceloleite.mercado.data.OrderData;

public class GetOrderResponse {

	private OrderData orderData		;

	public GetOrderResponse(OrderData orderData) {
		super();
		this.orderData = orderData;
	}

	public GetOrderResponse() {
		this(null);
	}

	public OrderData getOrder() {
		return orderData;
	}

	public void setOrder(OrderData orderData) {
		this.orderData = orderData;
	}
}
