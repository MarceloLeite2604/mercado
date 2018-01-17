package org.marceloleite.mercado.negotiationapi.model.cancelorder;

import org.marceloleite.mercado.negotiationapi.model.Order;

public class CancelOrderResponse {

	private Order order;

	public CancelOrderResponse(Order order) {
		super();
		this.order = order;
	}
	
	public CancelOrderResponse() {
		this(null);
	}

	public Order getOrder() {
		return order;
	}
	
	public void setOrder(Order order) {
		this.order = order;
	}
}
