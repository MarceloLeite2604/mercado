package org.marceloleite.mercado.retriever;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.converter.json.OrderbookConverter;
import org.marceloleite.mercado.databasemodel.Orderbook;
import org.marceloleite.mercado.siteretriever.OrderbookSiteRetriever;
import org.marceloleite.mercado.siteretriever.model.JsonOrderbook;

public class OrderbookRetriever {

	public Orderbook retrieve(Currency currency) {
		JsonOrderbook jsonOrderbook = new OrderbookSiteRetriever(currency).retrieve();
		return new OrderbookConverter().convert(jsonOrderbook);
	}
}
