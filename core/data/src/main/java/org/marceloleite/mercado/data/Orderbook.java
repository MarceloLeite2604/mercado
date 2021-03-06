package org.marceloleite.mercado.data;

import java.util.List;

public class Orderbook {
	
	private List<Offer> askOffers;
	
	private List<Offer> bidOffers;
	
	public Orderbook() {
		super();
	}

	public List<Offer> getAskOffers() {
		return askOffers;
	}

	public void setAskOffers(List<Offer> askOrders) {
		this.askOffers = askOrders;
	}

	public List<Offer> getBidOffers() {
		return bidOffers;
	}

	public void setBidOffers(List<Offer> bidOffers) {
		this.bidOffers = bidOffers;
	}
}
