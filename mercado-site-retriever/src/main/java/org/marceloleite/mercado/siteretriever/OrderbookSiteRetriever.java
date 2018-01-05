package org.marceloleite.mercado.siteretriever;

import javax.ws.rs.core.MediaType;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.interfaces.Retriever;
import org.marceloleite.mercado.jsonmodel.JsonOrderbook;

public class OrderbookSiteRetriever extends AbstractSiteRetriever implements Retriever<JsonOrderbook> {

	private static final String METHOD = "orderbook";

	public OrderbookSiteRetriever(Currency currency) {
		super(currency);
	}

	public JsonOrderbook retrieve(Object... args) {
		return createWebTarget().path(String.format(getPath(), getMethod()))
			.request(MediaType.APPLICATION_JSON)
			.get(JsonOrderbook.class);
	}

	@Override
	protected String getMethod() {
		return METHOD;
	}
}
