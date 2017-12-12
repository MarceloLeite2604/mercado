package org.marceloleite.mercado.consumer;

import javax.ws.rs.core.MediaType;

import org.marceloleite.mercado.consumer.model.Currency;
import org.marceloleite.mercado.consumer.model.JsonTicker;

public class TickerConsumer extends AbstractConsumer implements Consumer<JsonTicker> {
	
	private static final String METHOD = "ticker";
	
	public TickerConsumer(Currency cryptocoin) {
		super(cryptocoin);
	}	
	
	public JsonTicker consume(Object... args) {
		return createWebTarget()
			.path(String.format(getPath()))
			.request(MediaType.APPLICATION_JSON)
			.get(JsonTicker.class);
	}

	@Override
	protected String getMethod() {
		return METHOD;
	}
}
