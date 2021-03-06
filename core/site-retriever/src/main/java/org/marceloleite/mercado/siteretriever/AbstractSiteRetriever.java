package org.marceloleite.mercado.siteretriever;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.marceloleite.mercado.commons.Currency;

public abstract class AbstractSiteRetriever {

	private static final String TARGET_URL = "https://www.mercadobitcoin.net";

	private static final String PATH = "api/%s/%s/";

	protected Currency currency;

	public AbstractSiteRetriever(Currency currency) {
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
