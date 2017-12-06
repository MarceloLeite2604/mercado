package org.marceloleite.mercado.util.formatter;

import java.util.List;
import java.util.stream.Collectors;

import org.marceloleite.mercado.model.json.JsonOrderbook;
import org.marceloleite.mercado.model.persistence.Offer;
import org.marceloleite.mercado.model.persistence.Orderbook;

public class OrderbookFormatter implements Formatter<JsonOrderbook, Orderbook> {

	@Override
	public Orderbook format(JsonOrderbook jsonOrderbook) {
		OfferFormatter offerFormatter = new OfferFormatter();

		List<Offer> askOffers = jsonOrderbook.getAsks()
			.stream()
			.map(ask -> offerFormatter.format(ask))
			.collect(Collectors.toList());

		List<Offer> bidOffers = jsonOrderbook.getBids()
			.stream()
			.map(bid -> offerFormatter.format(bid))
			.collect(Collectors.toList());

		Orderbook orderbook = new Orderbook();
		orderbook.setAskOffers(askOffers);
		orderbook.setBidOffers(bidOffers);

		return orderbook;
	}

}
