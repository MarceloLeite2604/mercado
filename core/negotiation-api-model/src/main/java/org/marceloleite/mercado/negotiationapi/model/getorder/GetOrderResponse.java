package org.marceloleite.mercado.negotiationapi.model.getorder;

import org.marceloleite.mercado.negotiationapi.model.Order;

public class GetOrderResponse {

	private Order order;

	public GetOrderResponse(Order order) {
		super();
		this.order = order;
	}

	public GetOrderResponse() {
		this(null);
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
