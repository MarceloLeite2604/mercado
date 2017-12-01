package org.marceloleite.mercado;

import java.util.Calendar;

import org.marceloleite.mercado.consumer.OrderbookConsumer;
import org.marceloleite.mercado.consumer.TickerConsumer;
import org.marceloleite.mercado.consumer.TradesConsumer;
import org.marceloleite.mercado.model.Cryptocoin;
import org.marceloleite.mercado.model.Orderbook;
import org.marceloleite.mercado.model.Ticker;
import org.marceloleite.mercado.model.Trades;
import org.marceloleite.mercado.util.JsonFormatter;

public class Main {

	public static void main(String[] args) {
		Ticker ticker = new TickerConsumer(Cryptocoin.BCASH).consume();
		Orderbook orderbook = new OrderbookConsumer(Cryptocoin.BCASH).consume();
		
		Calendar now = Calendar.getInstance();
		Calendar pastFiveMinutes = ((Calendar)now.clone());
		pastFiveMinutes.add(Calendar.MINUTE, -5);
		Trades[] trades = new TradesConsumer(Cryptocoin.BCASH).consume(pastFiveMinutes, now);
		
		System.out.println(new JsonFormatter().format(trades));
	}
}
