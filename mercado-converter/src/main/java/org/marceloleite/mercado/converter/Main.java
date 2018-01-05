package org.marceloleite.mercado.converter;

import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.converter.json.OrderbookConverter;
import org.marceloleite.mercado.converter.json.TickerConverter;
import org.marceloleite.mercado.databasemodel.Orderbook;
import org.marceloleite.mercado.databasemodel.TickerPO;
import org.marceloleite.mercado.jsonmodel.JsonOrderbook;
import org.marceloleite.mercado.jsonmodel.JsonTicker;

public class Main {

	public static void main(String[] args) {
		ticker();
	}

	private static void ticker() {
		JsonTicker jsonTicker = null;
		TickerPO ticker = new TickerConverter().convertTo(jsonTicker);
		System.out.println(new ObjectToJsonConverter().convertTo(ticker));		
	}

	@SuppressWarnings("unused")
	private static void orderbook() {
		JsonOrderbook jsonOrderbook = null;
		Orderbook orderbook = new OrderbookConverter().convertTo(jsonOrderbook);
		System.out.println(new ObjectToJsonConverter().convertTo(orderbook));
	}
}
