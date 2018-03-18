package org.marceloleite.mercado.api.negotiation.methods.listorderbook;

import org.marceloleite.mercado.negotiationapi.model.listorderbook.Orderbook;

public class ListOrderbookResponse {

	private Orderbook orderbook;

	public ListOrderbookResponse(Orderbook orderbook) {
		super();
		this.orderbook = orderbook;
	}

	public ListOrderbookResponse() {
		this(null);
	}

	public Orderbook getOrderbook() {
		return orderbook;
	}

	public void setOrderbook(Orderbook orderbook) {
		this.orderbook = orderbook;
	}
}
