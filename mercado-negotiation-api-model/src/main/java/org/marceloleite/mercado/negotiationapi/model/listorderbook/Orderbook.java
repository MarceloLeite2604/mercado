package org.marceloleite.mercado.negotiationapi.model.listorderbook;

import java.util.List;

public class Orderbook {

	private List<OrderbookRegister> bids;

	private List<OrderbookRegister> asks;

	private long latestOrderId;

	public Orderbook(List<OrderbookRegister> bids, List<OrderbookRegister> asks, long latestOrderId) {
		super();
		this.bids = bids;
		this.asks = asks;
		this.latestOrderId = latestOrderId;
	}

	public Orderbook() {
		this(null, null, 0l);
	}

	public List<OrderbookRegister> getBids() {
		return bids;
	}

	public void setBids(List<OrderbookRegister> bids) {
		this.bids = bids;
	}

	public List<OrderbookRegister> getAsks() {
		return asks;
	}

	public void setAsks(List<OrderbookRegister> asks) {
		this.asks = asks;
	}

	public long getLatestOrderId() {
		return latestOrderId;
	}
	
	public void setLatestOrderId(long latestOrderId) {
		this.latestOrderId = latestOrderId;
	}

}
