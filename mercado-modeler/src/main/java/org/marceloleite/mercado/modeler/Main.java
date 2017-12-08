package org.marceloleite.mercado.modeler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;

import org.marceloleite.mercado.commons.util.ObjectToJsonFormatter;
import org.marceloleite.mercado.consumer.OrderbookConsumer;
import org.marceloleite.mercado.consumer.TickerConsumer;
import org.marceloleite.mercado.consumer.model.Cryptocoin;
import org.marceloleite.mercado.consumer.model.JsonOrderbook;
import org.marceloleite.mercado.consumer.model.JsonTicker;
import org.marceloleite.mercado.consumer.model.JsonTrade;
import org.marceloleite.mercado.consumer.trades.TradesRetriever;
import org.marceloleite.mercado.modeler.business.filter.TradeTypeFilter;
import org.marceloleite.mercado.modeler.persistence.Orderbook;
import org.marceloleite.mercado.modeler.persistence.Ticker;
import org.marceloleite.mercado.modeler.persistence.Trade;
import org.marceloleite.mercado.modeler.persistence.TradeType;
import org.marceloleite.mercado.modeler.util.formatter.MapTradeFormatter;
import org.marceloleite.mercado.modeler.util.formatter.OrderbookFormatter;
import org.marceloleite.mercado.modeler.util.formatter.TickerFormatter;

public class Main {

	public static void main(String[] args) {

		ticker();
		orderbook();
		trades();
	}

	private static void ticker() {
		JsonTicker jsonTicker = new TickerConsumer(Cryptocoin.BITCOIN).consume();
		Ticker ticker = new TickerFormatter().format(jsonTicker);
		System.out.println(new ObjectToJsonFormatter().format(ticker));
	}

	private static void orderbook() {
		JsonOrderbook jsonOrderbook = new OrderbookConsumer(Cryptocoin.BCASH).consume();
		Orderbook orderbook = new OrderbookFormatter().format(jsonOrderbook);
		System.out.println(new ObjectToJsonFormatter().format(orderbook));
	}

	private static void trades() {

		LocalDateTime endTime = LocalDateTime.now();
		LocalDateTime startTime = endTime.minus(Duration.ofHours(240));
		Duration duration = Duration.between(startTime, endTime);
		TradesRetriever tradesRetriever = new TradesRetriever();
		tradesRetriever.setStepDuration(Duration.ofMinutes(120));
		Map<Integer, JsonTrade> jsonTrades = tradesRetriever.retrieve(startTime, duration);
		Map<Integer, Trade> trades = new MapTradeFormatter().format(jsonTrades);
		System.out.println("Total retrieved: " + trades.size());

		Map<Integer, Trade> buyingTrades = new TradeTypeFilter(TradeType.BUY).filter(trades);
		Map<Integer, Trade> sellingTrades = new TradeTypeFilter(TradeType.SELL).filter(trades);

		double maxBuyingValue = trades.entrySet()
			.stream()
			.map(Entry<Integer, Trade>::getValue)
			.mapToDouble(Trade::getPrice)
			.max()
			.getAsDouble();
		double minBuyingValue = trades.entrySet()
			.stream()
			.map(Entry<Integer, Trade>::getValue)
			.mapToDouble(Trade::getPrice)
			.min()
			.getAsDouble();

		double totalNegotiated = trades.entrySet()
			.stream()
			.map(Entry<Integer, Trade>::getValue)
			.mapToDouble(Trade::getAmount)
			.sum();

		int lastTradeId = trades.entrySet()
			.stream()
			.map(Entry<Integer, Trade>::getValue)
			.mapToInt(Trade::getId)
			.max()
			.getAsInt();
		Trade lastTrade = trades.get(lastTradeId);

		int lastSellingTradeId = sellingTrades.entrySet()
			.stream()
			.map(Entry<Integer, Trade>::getValue)
			.mapToInt(Trade::getId)
			.max()
			.getAsInt();
		int lastBuyingTradeId = buyingTrades.entrySet()
			.stream()
			.map(Entry<Integer, Trade>::getValue)
			.mapToInt(Trade::getId)
			.max()
			.getAsInt();
		Trade lastSellingTrade = trades.get(lastSellingTradeId);
		Trade lastBuyingTrade = trades.get(lastBuyingTradeId);

		System.out.println("Max buying value: " + maxBuyingValue);
		System.out.println("Min buying value: " + minBuyingValue);
		System.out.println("Total negotiated: " + totalNegotiated);
		System.out.println("Last unit price: " + lastTrade.getPrice());
		System.out.println("Last buying unit price: " + lastSellingTrade.getPrice());
		System.out.println("Last selling unit price: " + lastBuyingTrade.getPrice());

	}
}
