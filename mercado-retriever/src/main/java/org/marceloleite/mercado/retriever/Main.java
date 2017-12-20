package org.marceloleite.mercado.retriever;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.modeler.persistence.dao.EntityManagerController;
import org.marceloleite.mercado.modeler.persistence.model.Trade;

public class Main {
	
	public static void main(String[] args) {
		// databaseTradesRetriever();
		tradesRetriever();
	}

	private static void tradesRetriever() {
		TradesRetriever tradesRetriever = new TradesRetriever();
		tradesRetriever.retrieve();
	}

	private static void databaseTradesRetriever() {
		DatabaseTradesRetriever databaseTradesRetriever = new DatabaseTradesRetriever();
		LocalDateTime end = LocalDateTime.now();
		LocalDateTime start = end.minus(Duration.ofHours(24));
		List<Trade> trades = databaseTradesRetriever.retrieve(start, end);
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		for (Trade trade : trades ) {
			System.out.println(objectToJsonConverter.convert(trade));
		}
		EntityManagerController.getInstance().close();
	}
}
