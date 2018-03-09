package org.marceloleite.mercado.negotiationapi.model.placesellorder;

import org.marceloleite.mercado.negotiationapi.model.Order;

public class PlaceSellOrderResult {

	private Order order;

	public PlaceSellOrderResult(Order order) {
		super();
		this.order = order;
	}
	
	public PlaceSellOrderResult() {
		this(null);
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
