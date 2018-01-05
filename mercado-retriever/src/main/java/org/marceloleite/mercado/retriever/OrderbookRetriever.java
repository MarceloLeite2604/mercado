package org.marceloleite.mercado.retriever;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.converter.json.OrderbookConverter;
import org.marceloleite.mercado.databasemodel.Orderbook;
import org.marceloleite.mercado.jsonmodel.JsonOrderbook;
import org.marceloleite.mercado.siteretriever.OrderbookSiteRetriever;

public class OrderbookRetriever {

	public Orderbook retrieve(Currency currency) {
		JsonOrderbook jsonOrderbook = new OrderbookSiteRetriever(currency).retrieve();
		return new OrderbookConverter().convertTo(jsonOrderbook);
	}
}
