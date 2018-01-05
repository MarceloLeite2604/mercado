package org.marceloleite.mercado.siteretriever;

import java.time.LocalDateTime;
import java.util.Map;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.jsonmodel.JsonOrderbook;
import org.marceloleite.mercado.jsonmodel.JsonTicker;
import org.marceloleite.mercado.jsonmodel.JsonTrade;
import org.marceloleite.mercado.siteretriever.trades.TradesSiteRetriever;

public class Main {

	public static void main(String[] args) {
		/*orderbookSiteRetriever();
		tickerSiteRetriever();*/
		tradesSiteRetriever();
	}
	
	@SuppressWarnings("unused")
	private static void orderbookSiteRetriever() {
		JsonOrderbook jsonOrderBook = new OrderbookSiteRetriever(Currency.BITCOIN).retrieve();
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		System.out.println(objectToJsonConverter.convertTo(jsonOrderBook));
	}
	
	@SuppressWarnings("unused")
	private static void tickerSiteRetriever() {
		JsonTicker jsonTicker = new TickerSiteRetriever(Currency.BITCOIN).retrieve();
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		System.out.println(objectToJsonConverter.convertTo(jsonTicker));
	}
	
	private static void tradesSiteRetriever() {
		LocalDateTime to = LocalDateTime.now();
		LocalDateTime from = LocalDateTime.from(to).minusDays(1); 
		Map<Long, JsonTrade> jsonTrades = new TradesSiteRetriever(Currency.BITCOIN).retrieve(from, to);
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		System.out.println(jsonTrades.size());
		/*System.out.println(objectToJsonConverter.convert(jsonTrades));*/
	}
}
