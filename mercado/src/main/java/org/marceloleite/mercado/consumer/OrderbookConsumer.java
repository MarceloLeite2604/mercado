package org.marceloleite.mercado.consumer;

import javax.ws.rs.core.MediaType;

import org.marceloleite.mercado.model.Cryptocoin;
import org.marceloleite.mercado.model.Orderbook;

public class OrderbookConsumer extends AbstractConsumer implements Consumer<Orderbook> {

	private static final String METHOD = "orderbook";

	public OrderbookConsumer(Cryptocoin cryptocoin) {
		super(cryptocoin);
	}

	public Orderbook consume(Object... args) {
		return createWebTarget().path(String.format(getPath(), getMethod()))
			.request(MediaType.APPLICATION_JSON)
			.get(Orderbook.class);
	}

	@Override
	protected String getMethod() {
		return METHOD;
	}
}
