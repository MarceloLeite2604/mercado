package org.marceloleite.mercado.retriever;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.converter.json.api.data.TickerConverter;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TickerPO;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTicker;
import org.marceloleite.mercado.siteretriever.TickerSiteRetriever;

public class TickerRetriever {

	public TickerPO retrieve(Currency currency) {
		JsonTicker jsonTicker = new TickerSiteRetriever(currency).retrieve();
		return new TickerConverter().convertTo(jsonTicker);
	}
}
