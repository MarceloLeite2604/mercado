package org.marceloleite.mercado.consumer;

import javax.ws.rs.core.MediaType;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.consumer.model.JsonOrderbook;

public class OrderbookConsumer extends AbstractConsumer implements Consumer<JsonOrderbook> {

	private static final String METHOD = "orderbook";

	public OrderbookConsumer(Currency cryptocoin) {
		super(cryptocoin);
	}

	public JsonOrderbook consume(Object... args) {
		return createWebTarget().path(String.format(getPath(), getMethod()))
			.request(MediaType.APPLICATION_JSON)
			.get(JsonOrderbook.class);
	}

	@Override
	protected String getMethod() {
		return METHOD;
	}
}
