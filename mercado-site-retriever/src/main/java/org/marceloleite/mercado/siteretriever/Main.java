package org.marceloleite.mercado.siteretriever;

import java.time.ZonedDateTime;
import java.util.Map;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.TimeInterval;
import org.marceloleite.mercado.commons.util.ZonedDateTimeUtils;
import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.data.Trade;
import org.marceloleite.mercado.jsonmodel.api.data.JsonOrderbook;
import org.marceloleite.mercado.jsonmodel.api.data.JsonTicker;
import org.marceloleite.mercado.siteretriever.trades.TradesSiteRetriever;

public class Main {

	public static void main(String[] args) {
		// orderbookSiteRetriever(); tickerSiteRetriever();
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
		ZonedDateTime to = ZonedDateTimeUtils.now();
		ZonedDateTime from = ZonedDateTime.from(to).minusDays(1);
		TimeInterval timeInterval = new TimeInterval(from, to);
		Map<Long, Trade> tradesDataModel = new TradesSiteRetriever(Currency.BITCOIN).retrieve(timeInterval);
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		System.out.println(tradesDataModel.size());
		System.out.println(objectToJsonConverter.convertTo(tradesDataModel));
	}
}
