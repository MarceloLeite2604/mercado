package org.marceloleite.mercado.databaseretriever;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.marceloleite.mercado.commons.Currency;
import org.marceloleite.mercado.commons.interfaces.Retriever;
import org.marceloleite.mercado.databasemodel.Tables;
import org.marceloleite.mercado.databasemodel.Trade;
import org.marceloleite.mercado.databasemodel.converter.CurrencyAttributeConverter;
import org.marceloleite.mercado.databaseretriever.persistence.EntityManagerController;

public class TradesDatabaseRetriever implements Retriever<Trade> {

	private static final String START_PARAMETER = "start";

	private static final String END_PARAMETER = "end";

	private static final String CURRENCY_PARAMETER = "currency";

	private static final String RETRIEVE_QUERY = "SELECT * FROM " + Tables.TRADE.getName() + " WHERE currency = :"
			+ CURRENCY_PARAMETER + " AND date BETWEEN :" + START_PARAMETER + " AND :" + END_PARAMETER;

	private EntityManager entityManager;

	private void createEntityManager() {
		if (entityManager == null) {
			entityManager = EntityManagerController.getInstance().createEntityManager();
		}
	}

	@Override
	public Trade retrieve(Object... args) {
		return null;
	}

	public List<Trade> retrieve(Currency currency, LocalDateTime start, LocalDateTime end) {
		createEntityManager();
		Query query = createNativeQuery(RETRIEVE_QUERY);
		query.setParameter(START_PARAMETER, start);
		query.setParameter(END_PARAMETER, end);
		query.setParameter(CURRENCY_PARAMETER, new CurrencyAttributeConverter().convertToDatabaseColumn(currency));
		List<Trade> trades = castObjectListToTradeList(query.getResultList());
		finishEntityManager();
		return trades;
	}

	private void finishEntityManager() {
		createEntityManager();
		entityManager.close();
	}

	public List<Trade> castObjectListToTradeList(List<?> objects) {
		List<Trade> trades = new ArrayList<>();

		for (Object object : objects) {
			if (object instanceof Trade) {
				trades.add((Trade) object);
			} else {
				throw new ClassCastException("Expecting object of type \"" + Trade.class.getName() + "\", but it is \""
						+ object.getClass().getName() + "\" instead.");
			}
		}

		return trades;
	}

	private Query createNativeQuery(String query) {
		createEntityManager();
		return entityManager.createNativeQuery(query, Trade.class);
	}

}
