package org.marceloleite.mercado.api.negotiation.methods.placesellorder;

import org.marceloleite.mercado.data.OrderData;

public class PlaceSellOrderResponse {

	private OrderData orderData;

	public PlaceSellOrderResponse(OrderData orderData) {
		super();
		this.orderData = orderData;
	}

	public PlaceSellOrderResponse() {
		this(null);
	}

	public OrderData getOrderData() {
		return orderData;
	}

	public void setOrder(OrderData orderData) {
		this.orderData = orderData;
	}
}
