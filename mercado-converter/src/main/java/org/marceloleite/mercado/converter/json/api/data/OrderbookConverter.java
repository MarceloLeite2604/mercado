package org.marceloleite.mercado.converter.json.api.data;

import java.util.List;
import java.util.stream.Collectors;

import org.marceloleite.mercado.base.model.Offer;
import org.marceloleite.mercado.base.model.Orderbook;
import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.jsonmodel.api.data.JsonOrderbook;

public class OrderbookConverter implements Converter<JsonOrderbook, Orderbook> {

	@Override
	public Orderbook convertTo(JsonOrderbook jsonOrderbook) {
		OfferConverter offerFormatter = new OfferConverter();

		List<Offer> askOffers = jsonOrderbook.getAsks()
			.stream()
			.map(ask -> offerFormatter.convertTo(ask))
			.collect(Collectors.toList());

		List<Offer> bidOffers = jsonOrderbook.getBids()
			.stream()
			.map(bid -> offerFormatter.convertTo(bid))
			.collect(Collectors.toList());

		Orderbook orderbook = new Orderbook();
		orderbook.setAskOffers(askOffers);
		orderbook.setBidOffers(bidOffers);

		return orderbook;
	}

	@Override
	public JsonOrderbook convertFrom(Orderbook object) {
		throw new UnsupportedOperationException();
	}

}
