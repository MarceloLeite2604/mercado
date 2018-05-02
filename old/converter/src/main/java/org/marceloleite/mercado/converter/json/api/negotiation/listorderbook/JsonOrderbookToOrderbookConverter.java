package org.marceloleite.mercado.converter.json.api.negotiation.listorderbook;

import java.util.ArrayList;
import java.util.List;

import org.marceloleite.mercado.commons.converter.Converter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.orderbook.JsonOrderbook;
import org.marceloleite.mercado.jsonmodel.api.negotiation.orderbook.JsonOrderbookRegister;
import org.marceloleite.mercado.negotiationapi.model.listorderbook.Orderbook;
import org.marceloleite.mercado.negotiationapi.model.listorderbook.OrderbookRegister;

public class JsonOrderbookToOrderbookConverter implements Converter<JsonOrderbook, Orderbook> {

	@Override
	public Orderbook convertTo(JsonOrderbook jsonOrderbook) {
		Orderbook orderbook = new Orderbook();
		List<JsonOrderbookRegister> jsonAsks = jsonOrderbook.getAsks();
		List<OrderbookRegister> asks = new ArrayList<>();
		JsonOrderbookRegisterToOrderbookRegisterConverter jsonOrderbookRegisterToOrderbookRegisterConverter = new JsonOrderbookRegisterToOrderbookRegisterConverter();
		if (jsonAsks != null && !jsonAsks.isEmpty()) {
			for (JsonOrderbookRegister jsonAsk : jsonAsks) {
				asks.add(jsonOrderbookRegisterToOrderbookRegisterConverter.convertTo(jsonAsk));
			}
		}
		orderbook.setAsks(asks);
		List<JsonOrderbookRegister> jsonBids = jsonOrderbook.getBids();
		List<OrderbookRegister> bids = new ArrayList<>();
		if (jsonBids != null && !jsonBids.isEmpty()) {
			for (JsonOrderbookRegister jsonBid : jsonAsks) {
				bids.add(jsonOrderbookRegisterToOrderbookRegisterConverter.convertTo(jsonBid));
			}
		}
		orderbook.setBids(bids);
		orderbook.setLatestOrderId(jsonOrderbook.getLatestOrderId());
		return orderbook;
	}

	@Override
	public JsonOrderbook convertFrom(Orderbook orderbook) {
		throw new UnsupportedOperationException();
	}

}
