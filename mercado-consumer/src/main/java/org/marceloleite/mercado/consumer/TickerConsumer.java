package org.marceloleite.mercado.consumer;

import javax.ws.rs.core.MediaType;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.consumer.model.JsonTicker;

public class TickerConsumer extends AbstractConsumer implements Consumer<JsonTicker> {
	
	private static final String METHOD = "ticker";
	
	public TickerConsumer(Currency currency) {
		super(currency);
	}	
	
	public JsonTicker consume(Object... args) {
		JsonTicker jsonTicker = createWebTarget()
			.path(String.format(getPath()))
			.request(MediaType.APPLICATION_JSON)
			.get(JsonTicker.class);
		jsonTicker.setCurrency(currency);
		return jsonTicker;
	}

	@Override
	protected String getMethod() {
		return METHOD;
	}
}
