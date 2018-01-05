package org.marceloleite.mercado.retriever;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.converter.json.TickerConverter;
import org.marceloleite.mercado.databasemodel.TickerPO;
import org.marceloleite.mercado.jsonmodel.JsonTicker;
import org.marceloleite.mercado.siteretriever.TickerSiteRetriever;

public class TickerRetriever {

	public TickerPO retrieve(Currency currency) {
		JsonTicker jsonTicker = new TickerSiteRetriever(currency).retrieve();
		return new TickerConverter().convertTo(jsonTicker);
	}
}
