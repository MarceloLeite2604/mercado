package org.marceloleite.mercado.converter;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.converter.json.OrderbookConverter;
import org.marceloleite.mercado.converter.json.TickerConverter;
import org.marceloleite.mercado.databasemodel.Orderbook;
import org.marceloleite.mercado.databasemodel.TickerPO;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;
import org.marceloleite.mercado.databaseretriever.persistence.dao.TickerDAO;
import org.marceloleite.mercado.siteretriever.OrderbookSiteRetriever;
import org.marceloleite.mercado.siteretriever.TickerSiteRetriever;
import org.marceloleite.mercado.siteretriever.model.JsonOrderbook;
import org.marceloleite.mercado.siteretriever.model.JsonTicker;

public class Main {

	public static void main(String[] args) {
		ticker();
	}

	private static void

 ticker() {
		JsonTicker jsonTicker = new TickerSiteRetriever(Currency.BITCOIN).retrieve();
		TickerPO ticker = new TickerConverter().convert(jsonTicker);
		System.out.println(new ObjectToJsonConverter().convert(ticker));
		
		TickerDAO tickerDAO = new TickerDAO();
		tickerDAO.merge(ticker);
		EntityManagerController.getInstance().close();
	}

	private static void orderbook() {
		JsonOrderbook jsonOrderbook = new OrderbookSiteRetriever(Currency.BCASH).retrieve();
		Orderbook orderbook = new OrderbookConverter().convert(jsonOrderbook);
		System.out.println(new ObjectToJsonConverter().convert(orderbook));
	}
}
