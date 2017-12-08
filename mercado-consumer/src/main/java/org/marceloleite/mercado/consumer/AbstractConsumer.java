package org.marceloleite.mercado.consumer;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.marceloleite.mercado.consumer.model.Cryptocoin;

public abstract class AbstractConsumer {

	private static final String TARGET_URL = "https://www.mercadobitcoin.net";

	private static final String PATH = "api/%s/%s/";

	protected Cryptocoin cryptocoin;

	public AbstractConsumer(Cryptocoin cryptocoin) {
		super();
		this.cryptocoin = cryptocoin;
	}

	protected WebTarget createWebTarget() {
		return ClientBuilder.newClient()
			.target(TARGET_URL);
	}

	protected String getPath() {
		return String.format(PATH, cryptocoin, getMethod());
	}
	
	protected abstract String getMethod();

}
