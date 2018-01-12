package org.marceloleite.mercado.negotiationapi.listorders;

import java.time.LocalDateTime;
import java.util.List;

import org.marceloleite.mercado.negotiationapi.model.Order;

public class ListOrdersMethodResponse extends AbstractTapiResponse {

	private List<Order> orders;

	public ListOrdersMethodResponse(long statusCode, LocalDateTime timestamp, List<Order> orders) {
		super(statusCode, timestamp);
		this.orders = orders;
	}

	public ListOrdersMethodResponse() {
		this(0l, null, null);
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
}
