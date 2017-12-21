package org.marceloleite.mercado.siteretriever;

import javax.ws.rs.core.MediaType;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.interfaces.Retriever;
import org.marceloleite.mercado.siteretriever.model.JsonTicker;

public class TickerSiteRetriever extends AbstractSiteRetriever implements Retriever<JsonTicker> {
	
	private static final String METHOD = "ticker";
	
	public TickerSiteRetriever(Currency currency) {
		super(currency);
	}	
	
	public JsonTicker retrieve(Object... args) {
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
