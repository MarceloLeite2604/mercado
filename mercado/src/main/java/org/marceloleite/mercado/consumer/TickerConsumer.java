package org.marceloleite.mercado.consumer;

import javax.ws.rs.core.MediaType;

import org.marceloleite.mercado.model.Cryptocoin;
import org.marceloleite.mercado.model.Ticker;

public class TickerConsumer extends AbstractConsumer implements Consumer<Ticker> {
	
	private static final String METHOD = "ticker";
	
	public TickerConsumer(Cryptocoin cryptocoin) {
		super(cryptocoin);
	}	
	
	public Ticker consume(Object... args) {
		return createWebTarget()
			.path(String.format(getPath()))
			.request(MediaType.APPLICATION_JSON)
			.get(Ticker.class);
	}

	@Override
	protected String getMethod() {
		return METHOD;
	}
}
