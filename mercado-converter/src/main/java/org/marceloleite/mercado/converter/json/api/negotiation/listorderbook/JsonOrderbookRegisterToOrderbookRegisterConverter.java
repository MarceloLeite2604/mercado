package org.marceloleite.mercado.converter.json.api.negotiation.listorderbook;

import org.marceloleite.mercado.commons.util.converter.Converter;
import org.marceloleite.mercado.jsonmodel.api.negotiation.orderbook.JsonOrderbookRegister;
import org.marceloleite.mercado.negotiationapi.model.listorderbook.OrderbookRegister;

public class JsonOrderbookRegisterToOrderbookRegisterConverter implements Converter<JsonOrderbookRegister, OrderbookRegister>{

	@Override
	public OrderbookRegister convertTo(JsonOrderbookRegister jsonOrderbookRegister) {
		OrderbookRegister orderbookRegister = new OrderbookRegister();
		orderbookRegister.setId(jsonOrderbookRegister.getOrderId());
		double limitPrice = Double.parseDouble(jsonOrderbookRegister.getLimitPrice());
		orderbookRegister.setLimitPrice(limitPrice);
		orderbookRegister.setOwner(jsonOrderbookRegister.getIsOwner());
		double quantity = Double.parseDouble(jsonOrderbookRegister.getQuantity());
		orderbookRegister.setQuantity(quantity);
		return orderbookRegister;
	}

	@Override
	public JsonOrderbookRegister convertFrom(OrderbookRegister orderbookRegister) {
		throw new UnsupportedOperationException();
	}

}
