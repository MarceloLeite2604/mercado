package org.marceloleite.mercado.util.formatter;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.model.json.JsonOrderbook;
import org.marceloleite.mercado.model.persistence.Offer;
import org.marceloleite.mercado.model.persistence.Orderbook;

public class OrderbookFormatter implements Formatter<JsonOrderbook, Orderbook> {

	@Override
	public Orderbook format(JsonOrderbook jsonOrderbook) {
		OfferFormatter offerFormatter = new OfferFormatter();

		List<List<Double>> asks = jsonOrderbook.getAsks();
		List<Offer> askOffers = new ArrayList<>();
		asks.stream()
			.forEach(ask -> askOffers.add(offerFormatter.format(ask)));

		List<List<Double>> bids = jsonOrderbook.getBids();
		List<Offer> bidOffers = new ArrayList<>();
		bids.stream()
			.forEach(bid -> bidOffers.add(offerFormatter.format(bid)));

		Orderbook orderbook = new Orderbook();
		orderbook.setAskOffers(askOffers);
		orderbook.setBidOffers(bidOffers);

		return orderbook;
	}

}
