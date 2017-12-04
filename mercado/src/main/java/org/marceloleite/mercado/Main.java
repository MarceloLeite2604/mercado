package org.marceloleite.mercado;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.marceloleite.mercado.business.filter.TradeTypeFilter;
import org.marceloleite.mercado.consumer.OrderbookConsumer;
import org.marceloleite.mercado.consumer.TickerConsumer;
import org.marceloleite.mercado.consumer.TradesConsumer;
import org.marceloleite.mercado.model.Cryptocoin;
import org.marceloleite.mercado.model.Orderbook;
import org.marceloleite.mercado.model.Ticker;
import org.marceloleite.mercado.model.Trade;
import org.marceloleite.mercado.util.formatter.LongToDateFormatter;
import org.marceloleite.mercado.util.formatter.ObjectToJsonFormatter;

public class Main {

	public static void main(String[] args) {
		Ticker ticker = new TickerConsumer(Cryptocoin.BCASH).consume();
		Orderbook orderbook = new OrderbookConsumer(Cryptocoin.BCASH).consume();
		
		Calendar now = Calendar.getInstance();
		Calendar pastFiveMinutes = ((Calendar)now.clone());
		pastFiveMinutes.add(Calendar.HOUR, -8);
		List<Trade> trades = new TradesConsumer(Cryptocoin.BCASH).consume(pastFiveMinutes, now);
		
		Date date = new LongToDateFormatter().format(ticker.getTicker().getDate());
		System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.S").format(date));
		
		System.out.println(new ObjectToJsonFormatter().format(trades));
		
		System.out.println("Total trades: " + trades.size());
		
		List<Trade> buyingTrades = new TradeTypeFilter("buy").filter(trades);
		List<Trade> sellingTrades = new TradeTypeFilter("sell").filter(trades);
		System.out.println("Total buying trades: " + buyingTrades.size());
		System.out.println("Total selling trades: " + sellingTrades.size());
		double totalAmountBought = buyingTrades.stream().mapToDouble(Trade::getAmount).sum();
		double totalAmountSold = sellingTrades.stream().mapToDouble(Trade::getAmount).sum();
		double averageBuingValue = buyingTrades.stream().mapToDouble(Trade::getPrice).average().getAsDouble();
		double averageSellingValue = sellingTrades.stream().mapToDouble(Trade::getPrice).average().getAsDouble();
		System.out.println("Total amount bought: " + totalAmountBought);
		System.out.println("Total amount sold: " + totalAmountSold);
		System.out.println("Total average buying value: " + averageBuingValue);
		System.out.println("Total average selling value: " + averageSellingValue);
		
		System.out.println("Total asks: " + orderbook.getAsks().size());
		System.out.println("Total bids: " + orderbook.getBids().size());
	}
}
