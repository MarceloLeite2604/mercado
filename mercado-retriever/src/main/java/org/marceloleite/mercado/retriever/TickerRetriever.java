package org.marceloleite.mercado.retriever;

import org.marceloleite.mercado.base.model.Ticker;
import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.converter.json.api.data.JsonTickerToTickerConverter;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTicker;
import org.marceloleite.mercado.siteretriever.TickerSiteRetriever;

public class TickerRetriever {

	public Ticker retrieve(Currency currency) {
		JsonTicker jsonTicker = new TickerSiteRetriever(currency).retrieve();
		return new JsonTickerToTickerConverter().convertTo(jsonTicker);
	}
}
