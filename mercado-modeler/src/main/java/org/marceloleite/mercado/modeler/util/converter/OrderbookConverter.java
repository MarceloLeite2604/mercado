package org.marceloleite.mercado.modeler.util.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.marceloleite.mercado.commons.interfaces.Converter;
import org.marceloleite.mercado.consumer.model.JsonOrderbook;
import org.marceloleite.mercado.modeler.persistence.model.Offer;
import org.marceloleite.mercado.modeler.persistence.model.Orderbook;

public class OrderbookConverter implements Converter<JsonOrderbook, Orderbook> {

	@Override
	public Orderbook convert(JsonOrderbook jsonOrderbook) {
		OfferConverter offerFormatter = new OfferConverter();

		List<Offer> askOffers = jsonOrderbook.getAsks()
			.stream()
			.map(ask -> offerFormatter.convert(ask))
			.collect(Collectors.toList());

		List<Offer> bidOffers = jsonOrderbook.getBids()
			.stream()
			.map(bid -> offerFormatter.convert(bid))
			.collect(Collectors.toList());

		Orderbook orderbook = new Orderbook();
		orderbook.setAskOffers(askOffers);
		orderbook.setBidOffers(bidOffers);

		return orderbook;
	}

}
