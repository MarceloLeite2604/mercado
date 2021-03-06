package org.marceloleite.mercado.databaseretriever;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.converter.ObjectToJsonConverter;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;
import org.marceloleite.mercado.databaseretriever.persistence.daos.TradeDAO;
import org.marceloleite.mercado.databaseretriever.persistence.objects.TradePO;

public class Main {

	public static void main(String[] args) {
		tradesDatabaseRetriever();
		EntityManagerController.getInstance().close();
	}

	@SuppressWarnings("unused")
	private static void tradesDatabaseRetriever() {
		TradeDAO tradesDAO = new TradeDAO();
		ZonedDateTime end = ZonedDateTime.of(2017, 01, 01, 0, 0, 0, 0, ZoneOffset.UTC);
		ZonedDateTime start = ZonedDateTime.from(end).minusDays(60);
		List<TradePO> trades = tradesDAO.retrieve(Currency.BITCOIN, start, end);
		ObjectToJsonConverter objectToJsonConverter = new ObjectToJsonConverter();
		System.out.println(objectToJsonConverter.convertTo(trades));
	}
}
