package org.marceloleite.mercado.siteretriever;

import javax.ws.rs.core.MediaType;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.jsonmodel.JsonTicker;

public class TickerSiteRetriever extends AbstractSiteRetriever {
	
	private static final String METHOD = "ticker";
	
	public TickerSiteRetriever(Currency currency) {
		super(currency);
	}	
	
	public JsonTicker retrieve() {
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
