package org.marceloleite.mercado.api.negotiation.methods.cancelorder;

import org.marceloleite.mercado.data.OrderData;

public class CancelOrderResponse {

	private OrderData orderData;

	public CancelOrderResponse(OrderData orderData) {
		super();
		this.orderData = orderData;
	}
	
	public CancelOrderResponse() {
		this(null);
	}

	public OrderData getOrderData() {
		return orderData;
	}
	
	public void setOrderData(OrderData orderData) {
		this.orderData = orderData;
	}
}
