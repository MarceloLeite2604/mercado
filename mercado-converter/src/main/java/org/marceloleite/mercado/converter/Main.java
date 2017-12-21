package org.marceloleite.mercado.converter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.converter.business.filter.TradeTypeFilter;
import org.marceloleite.mercado.databasemodel.Orderbook;
import org.marceloleite.mercado.databasemodel.Ticker;
import org.marceloleite.mercado.databasemodel.Trade;
import org.marceloleite.mercado.databasemodel.TradeType;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;
import org.marceloleite.mercado.databaseretriever.persistence.dao.TickerDAO;
import org.marceloleite.mercado.siteretriever.OrderbookSiteRetriever;
import org.marceloleite.mercado.siteretriever.TickerSiteRetriever;
import org.marceloleite.mercado.siteretriever.model.JsonOrderbook;
import org.marceloleite.mercado.siteretriever.model.JsonTicker;
import org.marceloleite.mercado.siteretriever.model.JsonTrade;
import org.marceloleite.mercado.siteretriever.trades.TradesSiteRetrieverController;

public class Main {

	public static void main(String[] args) {
		ticker();
	}

	private static void ticker() {
		JsonTicker jsonTicker = new TickerSiteRetriever(Currency.BITCOIN).retrieve();
		Ticker ticker = new TickerConverter().convert(jsonTicker);
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

	private static void trades() {

		LocalDateTime endTime = LocalDateTime.now();
		LocalDateTime startTime = endTime.minus(Duration.ofHours(24));
		Duration duration = Duration.between(startTime, endTime);
		TradesSiteRetrieverController tradesRetriever = new TradesSiteRetrieverController();
		tradesRetriever.setStepDuration(Duration.ofMinutes(10));
		Map<Integer, JsonTrade> jsonTrades = tradesRetriever.retrieve(Currency.BITCOIN, startTime, duration);
		Map<Integer, Trade> trades = new MapTradeConverter().convert(jsonTrades);
		System.out.println("Total retrieved: " + trades.size());

		Map<Integer, Trade> buyingTrades = new TradeTypeFilter(TradeType.BUY).filter(trades);
		Map<Integer, Trade> sellingTrades = new TradeTypeFilter(TradeType.SELL).filter(trades);

		double maxValue = trades.entrySet().stream().map(Entry<Integer, Trade>::getValue).mapToDouble(Trade::getPrice)
				.max().getAsDouble();
		double minValue = trades.entrySet().stream().map(Entry<Integer, Trade>::getValue).mapToDouble(Trade::getPrice)
				.min().getAsDouble();

		double totalNegotiated = trades.entrySet().stream().map(Entry<Integer, Trade>::getValue)
				.mapToDouble(Trade::getAmount).sum();

		int lastTradeId = trades.entrySet().stream().map(Entry<Integer, Trade>::getValue).mapToInt(Trade::getId).max()
				.getAsInt();
		Trade lastTrade = trades.get(lastTradeId);

		int lastSellingTradeId = sellingTrades.entrySet().stream().map(Entry<Integer, Trade>::getValue)
				.mapToInt(Trade::getId).max().getAsInt();
		int lastBuyingTradeId = buyingTrades.entrySet().stream().map(Entry<Integer, Trade>::getValue)
				.mapToInt(Trade::getId).max().getAsInt();
		Trade lastSellingTrade = trades.get(lastSellingTradeId);
		Trade lastBuyingTrade = trades.get(lastBuyingTradeId);

		System.out.println("Max buying value: " + maxValue);
		System.out.println("Min buying value: " + minValue);
		System.out.println("Total negotiated: " + totalNegotiated);
		System.out.println("Last unit price: " + lastTrade.getPrice());
		System.out.println("Last buying unit price: " + lastSellingTrade.getPrice());
		System.out.println("Last selling unit price: " + lastBuyingTrade.getPrice());

	}
}
