package org.marceloleite.mercado.siteretriever;

import java.time.LocalDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.siteretriever.model.JsonOrderbook;
import org.marceloleite.mercado.siteretriever.model.JsonTicker;
import org.marceloleite.mercado.siteretriever.model.JsonTrade;
import org.marceloleite.mercado.siteretriever.trades.TradesSiteRetriever;

public class Main {

	public static void main(String[] args) {
		orderbookSiteRetriever();
		tickerSiteRetriever();
		tradesSiteRetriever();
	}
	
	private static void orderbookSiteRetriever() {
		JsonOrderbook jsonOrderBook = new OrderbookSiteRetriever(Currency.BITCOIN).retrieve();
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		System.out.println(objectToJsonConverter.convert(jsonOrderBook));
	}
	
	private static void tickerSiteRetriever() {
		JsonTicker jsonTicker = new TickerSiteRetriever(Currency.BITCOIN).retrieve();
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		System.out.println(objectToJsonConverter.convert(jsonTicker));
	}
	
	private static void tradesSiteRetriever() {
		LocalDateTime to = LocalDateTime.now();
		LocalDateTime from = LocalDateTime.from(to).minusSeconds(60); 
		List<JsonTrade> jsonTrades = new TradesSiteRetriever(Currency.BITCOIN).retrieve(from, to);
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		System.out.println(objectToJsonConverter.convert(jsonTrades));
	}
}
