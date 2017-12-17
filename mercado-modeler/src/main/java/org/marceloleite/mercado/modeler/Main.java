package org.marceloleite.mercado.modeler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;

import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.consumer.OrderbookConsumer;
import org.marceloleite.mercado.consumer.TickerConsumer;
import org.marceloleite.mercado.consumer.model.Currency;
import org.marceloleite.mercado.consumer.model.JsonOrderbook;
import org.marceloleite.mercado.consumer.model.JsonTicker;
import org.marceloleite.mercado.consumer.model.JsonTrade;
import org.marceloleite.mercado.consumer.trades.TradesRetriever;
import org.marceloleite.mercado.modeler.business.filter.TradeTypeFilter;
import org.marceloleite.mercado.modeler.persistence.dao.EntityManagerController;
import org.marceloleite.mercado.modeler.persistence.dao.TickerDAO;
import org.marceloleite.mercado.modeler.persistence.model.Orderbook;
import org.marceloleite.mercado.modeler.persistence.model.Ticker;
import org.marceloleite.mercado.modeler.persistence.model.Trade;
import org.marceloleite.mercado.modeler.persistence.model.TradeType;
import org.marceloleite.mercado.modeler.util.converter.MapTradeConverter;
import org.marceloleite.mercado.modeler.util.converter.OrderbookConverter;
import org.marceloleite.mercado.modeler.util.converter.TickerConverter;

public class Main {

	public static void main(String[] args) {

		ticker();
		// orderbook();
		// trades();
		EntityManagerController.getInstance().close();
	}

	private static void ticker() {
		JsonTicker jsonTicker = new TickerConsumer(Currency.BITCOIN).consume();
		Ticker ticker = new TickerConverter().format(jsonTicker);
		System.out.println(new ObjectToJsonConverter().format(ticker));
		
		TickerDAO tickerDAO = new TickerDAO();
		tickerDAO.merge(ticker);
	}

	private static void orderbook() {
		JsonOrderbook jsonOrderbook = new OrderbookConsumer(Currency.BCASH).consume();
		Orderbook orderbook = new OrderbookConverter().format(jsonOrderbook);
		System.out.println(new ObjectToJsonConverter().format(orderbook));
	}

	private static void trades() {

		LocalDateTime endTime = LocalDateTime.now();
		LocalDateTime startTime = endTime.minus(Duration.ofHours(24));
		Duration duration = Duration.between(startTime, endTime);
		TradesRetriever tradesRetriever = new TradesRetriever();
		tradesRetriever.setStepDuration(Duration.ofMinutes(10));
		Map<Integer, JsonTrade> jsonTrades = tradesRetriever.retrieve(Currency.BITCOIN, startTime, duration);
		Map<Integer, Trade> trades = new MapTradeConverter().format(jsonTrades);
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
