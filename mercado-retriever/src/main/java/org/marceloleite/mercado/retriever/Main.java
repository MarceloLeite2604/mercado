package org.marceloleite.mercado.retriever;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;

public class Main {

	public static void main(String[] args) {
		tradesRetriever();
	}

	private static void tradesRetriever() {
		try {
			LocalDateTime end = LocalDateTime.now();
			LocalDateTime start = end.minus(Duration.ofSeconds(30));
			TradesRetriever tradesRetriever = new TradesRetriever();
			List<TradePO> trades = tradesRetriever.retrieve(Currency.BITCOIN, start, end, false);
			if (trades.size() > 0) {
				System.out.println(trades.get(trades.size() - 1).getPrice());
			}
			EntityManagerController.getInstance().close();
		} finally {
			EntityManagerController.getInstance().close();
		}
	}	
}
