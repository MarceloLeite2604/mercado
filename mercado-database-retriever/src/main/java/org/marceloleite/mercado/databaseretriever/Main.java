package org.marceloleite.mercado.databaseretriever;

import java.time.LocalDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.databasemodel.Trade;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;

public class Main {

	public static void main(String[] args) {
		tradesDatabaseRetriever();
		EntityManagerController.getInstance().close();
	}

	private static void tradesDatabaseRetriever() {
		TradesDatabaseRetriever tradesDatabaseRetriever = new TradesDatabaseRetriever();
		LocalDateTime end = LocalDateTime.now();
		LocalDateTime start = LocalDateTime.from(end).minusSeconds(60);
		List<Trade> trades = tradesDatabaseRetriever.retrieve(Currency.BITCOIN, start, end);
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		System.out.println(objectToJsonConverter.convert(trades));
	}
}
