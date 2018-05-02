package org.marceloleite.mercado.api.negotiation.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Orderbook {

	private List<OrderbookRegister> bids;

	private List<OrderbookRegister> asks;

	@JsonProperty("lastest_order_id")
	private long latestOrderId;

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
