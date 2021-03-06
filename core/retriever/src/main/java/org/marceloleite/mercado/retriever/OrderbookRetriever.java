package org.marceloleite.mercado.retriever;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.converter.json.api.data.OrderbookConverter;
import org.marceloleite.mercado.data.Orderbook;
import org.marceloleite.mercado.jsonmodel.api.data.JsonOrderbook;
import org.marceloleite.mercado.siteretriever.OrderbookSiteRetriever;

public class OrderbookRetriever {

	public Orderbook retrieve(Currency currency) {
		JsonOrderbook jsonOrderbook = new OrderbookSiteRetriever(currency).retrieve();
		return new OrderbookConverter().convertTo(jsonOrderbook);
	}
}
