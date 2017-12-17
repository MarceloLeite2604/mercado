package org.marceloleite.mercado.consumer;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.marceloleite.mercado.consumer.model.Currency;

public abstract class AbstractConsumer {

	private static final String TARGET_URL = "https://www.mercadobitcoin.net";

	private static final String PATH = "api/%s/%s/";

	protected Currency currency;

	public AbstractConsumer(Currency currency) {
		super();
		this.currency = currency;
	}

	protected WebTarget createWebTarget() {
		return ClientBuilder.newClient()
			.target(TARGET_URL);
	}

	protected String getPath() {
		return String.format(PATH, currency, getMethod());
	}
	
	protected abstract String getMethod();

}
