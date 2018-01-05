package org.marceloleite.mercado.databaseretriever;

import java.time.LocalDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.util.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.databasemodel.TradePO;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;
import org.marceloleite.mercado.databaseretriever.persistence.dao.TradeDAO;

public class Main {

	public static void main(String[] args) {
		tradesDatabaseRetriever();
		EntityManagerController.getInstance().close();
	}

	private static void tradesDatabaseRetriever() {
		TradeDAO tradesDAO = new TradeDAO();
		LocalDateTime end = LocalDateTime.of(2017, 01, 01, 00, 00);
		LocalDateTime start = LocalDateTime.from(end).minusDays(60);
		List<TradePO> trades = tradesDAO.retrieve(Currency.BITCOIN, start, end);
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		System.out.println(objectToJsonConverter.convertTo(trades));
	}
}
