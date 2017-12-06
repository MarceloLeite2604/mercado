package org.marceloleite.mercado.consumer;

import javax.ws.rs.core.MediaType;

import org.marceloleite.mercado.model.Cryptocoin;
import org.marceloleite.mercado.model.json.JsonOrderbook;

public class OrderbookConsumer extends AbstractConsumer implements Consumer<JsonOrderbook> {

	private static final String METHOD = "orderbook";

	public OrderbookConsumer(Cryptocoin cryptocoin) {
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
