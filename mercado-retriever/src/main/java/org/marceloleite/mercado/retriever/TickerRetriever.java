package org.marceloleite.mercado.retriever;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.converter.TickerConverter;
import org.marceloleite.mercado.databasemodel.Ticker;
import org.marceloleite.mercado.siteretriever.TickerSiteRetriever;
import org.marceloleite.mercado.siteretriever.model.JsonTicker;

public class TickerRetriever {

	public Ticker retrieve(Currency currency) {
		JsonTicker jsonTicker = new TickerSiteRetriever(currency).retrieve();
		return new TickerConverter().convert(jsonTicker);
	}
}
