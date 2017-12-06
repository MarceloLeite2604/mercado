package org.marceloleite.mercado;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.marceloleite.mercado.business.TradesRetriever;
import org.marceloleite.mercado.business.filter.TradeTypeFilter;
import org.marceloleite.mercado.consumer.OrderbookConsumer;
import org.marceloleite.mercado.consumer.TickerConsumer;
import org.marceloleite.mercado.model.Cryptocoin;
import org.marceloleite.mercado.model.json.JsonOrderbook;
import org.marceloleite.mercado.model.json.JsonTicker;
import org.marceloleite.mercado.model.json.JsonTrade;
import org.marceloleite.mercado.model.persistence.Orderbook;
import org.marceloleite.mercado.model.persistence.Ticker;
import org.marceloleite.mercado.model.persistence.Trade;
import org.marceloleite.mercado.model.persistence.TradeType;
import org.marceloleite.mercado.util.formatter.ListTradeFormatter;
import org.marceloleite.mercado.util.formatter.MapTradeFormatter;
import org.marceloleite.mercado.util.formatter.ObjectToJsonFormatter;
import org.marceloleite.mercado.util.formatter.OrderbookFormatter;
import org.marceloleite.mercado.util.formatter.TickerFormatter;

public class Main {

	public static void main(String[] args) {

		ticker();
		// orderbook();
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
		System.out.println(new ObjectToJsonFormatter().format(jsonOrderbook));
		System.out.println(new ObjectToJsonFormatter().format(orderbook));
	}

	private static void trades() {

		LocalDate now = LocalDate.now();
		LocalDate past24hours = now.minus(Duration.ofHours(24));
		Duration duration = Duration.between(past24hours, now);
		List<JsonTrade> jsonTrades = new TradesRetriever().retrieve(48, 30, Calendar.MINUTE);
		List<Trade> trades = new ListTradeFormatter().format(jsonTrades);

		List<Trade> buyingTrades = new TradeTypeFilter(TradeType.BUY).filter(trades);
		List<Trade> sellingTrades = new TradeTypeFilter(TradeType.SELL).filter(trades);

		double maxBuyingValue = trades.stream()
			.mapToDouble(Trade::getPrice)
			.max()
			.getAsDouble();
		double minBuyingValue = trades.stream()
			.mapToDouble(Trade::getPrice)
			.min()
			.getAsDouble();

		double totalNegotiated = trades.stream()
			.mapToDouble(Trade::getAmount)
			.sum();
		
		int lastTradeId = trades.stream().mapToInt(Trade::getId).max().getAsInt();
		Map<Integer, Trade> tradesMap = new MapTradeFormatter().format(jsonTrades);
		Trade lastTrade = tradesMap.get(lastTradeId);
		
		int lastSellingTradeId = sellingTrades.stream().mapToInt(Trade::getId).max().getAsInt();
		int lastBuyingTradeId = buyingTrades.stream().mapToInt(Trade::getId).max().getAsInt();
		Trade lastSellingTrade = tradesMap.get(lastSellingTradeId);
		Trade lastBuyingTrade = tradesMap.get(lastBuyingTradeId);
		
		System.out.println("Max buying value: " + maxBuyingValue);
		System.out.println("Min buying value: " + minBuyingValue);
		System.out.println("Total negotiated: " + totalNegotiated);
		System.out.println("Last unit price: " + lastTrade.getPrice());
		System.out.println("Last buying unit price: " + lastSellingTrade.getPrice());
		System.out.println("Last selling unit price: " + lastBuyingTrade.getPrice());
				
	}
}
