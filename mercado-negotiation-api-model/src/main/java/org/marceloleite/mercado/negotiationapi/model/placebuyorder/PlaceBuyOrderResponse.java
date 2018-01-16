package org.marceloleite.mercado.negotiationapi.model.placebuyorder;

import org.marceloleite.mercado.negotiationapi.model.listorders.Order;

public class PlaceBuyOrderResponse {

	private Order order;

	public PlaceBuyOrderResponse(Order order) {
		super();
		this.order = order;
	}

	public PlaceBuyOrderResponse() {
		this(null);
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
